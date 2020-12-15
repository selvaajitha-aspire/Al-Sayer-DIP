import { ToastrService } from 'ngx-toastr';

import { Component, OnInit, ViewChild, ChangeDetectionStrategy, NgZone, ChangeDetectorRef } from '@angular/core';
import {} from 'googlemaps';
import {
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { RoadsideAssistanceService } from '../../services/roadside-assistance/roadside-assistance.service';
import { IssueTypes } from '../models/issue-type.model';
import { RoutingService } from '@spartacus/core';
import { CommonService } from "../../services/common/common.services";

declare var $: any;

@Component({
  selector: 'app-roadside-assistance',
  templateUrl: './roadside-assistance.component.html',
  styleUrls: ['./roadside-assistance.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RoadsideAssistanceComponent implements OnInit {

  @ViewChild('map', { static: true }) mapElement: any;
  map: google.maps.Map;
  private lat: any = 29.378586 ;
  private lng: any = 47.990341;
  latLng=new google.maps.LatLng(this.lat,this.lng);
  currentLatLng;
  driverLatLng;
  autocomplete:google.maps.places.Autocomplete;
  user: any;
  vehicleList;
  driverDetails;
  warrantyFlag: boolean = true;
  fileName: String = "Choose file";
  addressU='';
  marker1:google.maps.Marker;
  marker2:google.maps.Marker;
  public IssueTypes = IssueTypes;
  ReqJson: any = {};
  directionsService: google.maps.DirectionsService;
  directionsRenderer: google.maps.DirectionsRenderer;
  originIcon = 'https://chart.googleapis.com/chart?' +
            'chst=d_map_pin_letter&chld=O|FFFF00|000000';
 
 // issueTypes: Array<string> = Object.keys(IssueTypes).filter(key => isNaN(+key));
  private autocomplete_init: boolean=false;
  geocoder = new google.maps.Geocoder();
  rsaForm: FormGroup = this.fb.group(
    {
      vehicle: this.fb.group({
        chassisNumber:['',Validators.required]
      }),
      issue: ['',Validators.required],
      latitude: ['', Validators.required],
      longitude: ['', Validators.required],
      //addressU: [''],
      notes: ['',Validators.required],
      attachments: ['']
    }
  );
  constructor(private assistanceService : RoadsideAssistanceService,
    public commonService:CommonService,protected fb: FormBuilder, 
     private ngZone: NgZone, protected router: RoutingService,
     private toastr: ToastrService, private cd: ChangeDetectorRef) {
    
  }

  ngOnInit(): void {
    this.vehicleList=this.assistanceService.getVehicles() || [];
    //this.commonService.getObservableWithParam(this,"populateVehicles","getItems","vehicleList");
    const mapProperties = {
      center:this.latLng ,
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP
   };
   this.map = new google.maps.Map(this.mapElement.nativeElement, mapProperties);
   new google.maps.Marker({
     position:this.latLng ,
     map: this.map,
     title: "Static Location",
     icon : this.originIcon
   });
   
    
 }

 populateVehicles(vehicles){
    this.vehicleList = vehicles;
    
 }

 getCurrentLocation(){
   
   this.assistanceService.getPosition().then(pos=>
     {
       const currentLatLng=new google.maps.LatLng(pos.lat, pos.lng);
       const mapProperties = {
         center: currentLatLng,
         zoom: 15,
         mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      this.map = new google.maps.Map(this.mapElement.nativeElement, mapProperties);
       var marker1=new google.maps.Marker({
       position:currentLatLng,
       map: this.map,
       animation: google.maps.Animation.DROP,
       title: "Your current location!",
       draggable: true
     });
      this.rsaForm.get("latitude").patchValue(`${pos.lat}`);
      this.rsaForm.get("longitude").patchValue(`${pos.lng}`);
      this.currentLatLng=currentLatLng;
      this.getAddress();
      google.maps.event.addListener(marker1, "dragend",  (mEvent)=>{
         this.ngZone.run(() => {
          this.rsaForm.get("latitude").patchValue(mEvent.latLng.lat());          
          this.rsaForm.get("longitude").patchValue(mEvent.latLng.lng());
          this.currentLatLng=new google.maps.LatLng(mEvent.latLng.lat(), mEvent.latLng.lng());
          this.getAddress();
        });
      });
     });
     $("#locationPopup").modal('show');
 }

 getAddress(){      
  this.geocoder.geocode({'location':this.currentLatLng}, (results, status)=> {
      if (status == google.maps.GeocoderStatus.OK) {
          if (results[0]) {
            var address=results[0].formatted_address;
           this.addressU=address;
           this.cd.detectChanges();
           //this.rsaForm.get('addressU').patchValue(this.addressU);
          } else {
            console.log(('Location not found'));
          }
      } else {
        console.log(('Geocoder failed due to: ' + status));
      }
    }
  )};

 getDriverDetails(){
  this.driverDetails=this.assistanceService.getDriverDetailsPro().then(pos=>
    {
      const driverLatLng=new google.maps.LatLng(pos.lng, pos.lat);
      this.driverLatLng=driverLatLng;
      this.marker2=new google.maps.Marker({
        position:driverLatLng,
        map: this.map,
        title: `${pos.name} is here`
      });
      this.calculateAndDisplayRoute();
    })   
  
 }
  
 calculateAndDisplayRoute() {
  var directionsService = new google.maps.DirectionsService();
  var directionsRenderer= new google.maps.DirectionsRenderer();
 directionsRenderer.setMap(this.map);
 const stepDisplay = new google.maps.InfoWindow();
      const markerArray = [];
  directionsService.route(
    {
      origin:  this.driverLatLng,
      destination: this.currentLatLng,
      travelMode: google.maps.TravelMode.DRIVING,
      avoidHighways:true
    },
    (response, status) => {
      if (status === "OK") {
        directionsRenderer.setDirections(response);
        this.showSteps(response, markerArray, stepDisplay, this.map);
        this.getDuration();
      } else {
        window.alert("Directions request failed due to " + status);
      }
    }
  );
}


showSteps(directionResult, markerArray, stepDisplay, map) {
  // For each step, place a marker, and add the text to the marker's infowindow.
  // Also attach the marker to an array so we can keep track of it and remove it
  // when calculating new routes.
  const myRoute = directionResult.routes[0].legs[0];
  
  for (let i = 0; i < myRoute.steps.length; i++) {
    const marker = (markerArray[i] =
      markerArray[i] || new google.maps.Marker());
    marker.setMap(this.map);
    marker.setPosition(myRoute.steps[i].start_location);
    this.attachInstructionText(
      stepDisplay,
      marker,
      myRoute.steps[i].instructions,
      this.map
    );
  }
  
}

 attachInstructionText(stepDisplay, marker, text, map) {
  google.maps.event.addListener(marker, "click", () => {
    // Open an info window when the marker is clicked on, containing the text
    // of the step.
    stepDisplay.setContent(text);
    stepDisplay.open(map, marker);
  });
}

getDuration(){
  const service = new google.maps.DistanceMatrixService();
  const geocoder = new google.maps.Geocoder();
  const bounds = new google.maps.LatLngBounds();
  service.getDistanceMatrix(
    {
      origins: [this.driverLatLng],
      destinations: [this.currentLatLng],
      travelMode: google.maps.TravelMode.DRIVING,
      unitSystem: google.maps.UnitSystem.METRIC,
      avoidHighways: false,
      avoidTolls: false,
    },
    (response, status) => {
      if (status !== "OK") {
        alert("Error was: " + status);
      } else {
        const originList = response.originAddresses;
        const destinationList = response.destinationAddresses;
        const results = response.rows[0].elements;           
          alert(
            // originList[0] +
            // " to " +
            // destinationList[0] +
            // ": " +
            results[0].distance.text +
            "Your driver will reach in" +
            results[0].duration.text );                
              
      }
    }
  );
}

getSelectedVehicle(chassisNumber) {
  this.vehicleList.subscribe(vehicles => {
    vehicles.map(vehicle => {
      if(vehicle.chassisNumber === chassisNumber) {
        if(vehicle.warranties) {
          this.warrantyFlag = false;
          vehicle.warranties.map(warranty => {
            const warrantyTypeFlag = warranty.warrantyType && warranty.warrantyType.includes('14');
            const warrantyDate = warranty.warrantyExpiryDate;
            const warrantyDateFlag = new Date() < new Date(warrantyDate) ? true : false;
            if(warrantyTypeFlag && warrantyDateFlag) {
              this.warrantyFlag = true;
            }
          })
        } else {
          this.warrantyFlag = false;
        }
      }
    });
    this.cd.detectChanges();
  })
}


// autocompleteFocus() {
//   alert("Hello Auto")
//  this.autocomplete_init = true;
//  if (!this.autocomplete_init) {
  
//   var input = document.getElementById("latitude") as HTMLInputElement;
//   this.autocomplete = new google.maps.places.Autocomplete(input);
//    alert(input);
//   this.autocomplete.bindTo("bounds", this.map);
//   this.autocomplete.setFields(["address_components", "geometry", "icon", "name"]);
//   const infowindow = new google.maps.InfoWindow();
//   const infowindowContent = document.getElementById(
//     "infowindow-content"
//   ) as HTMLElement;
//   infowindow.setContent(infowindowContent);
//   this.autocomplete.addListener("place_changed", () => {
   
//    this.marker2.setVisible(false);
//    const place = this.autocomplete.getPlace();

//    if (!place.geometry) {
//      // User entered the name of a Place that was not suggested and
//      // pressed the Enter key, or the Place Details request failed.
//      window.alert("No details available for input: '" + place.name + "'");
//      return;
//    }

//    // If the place has a geometry, then present it on a map.
//    if (place.geometry.viewport) {
//      this.map.fitBounds(place.geometry.viewport);
//    } else {
//      this.map.setCenter(place.geometry.location);
//      this.map.setZoom(17);
//    }
//    this.marker2.setPosition(place.geometry.location);
//     this.marker2.setVisible(true);
//     let address = "";

//     if (place.address_components) {
//       address = [
//         (place.address_components[0] &&
//           place.address_components[0].short_name) ||
//           "",
//         (place.address_components[1] &&
//           place.address_components[1].short_name) ||
//           "",
//         (place.address_components[2] &&
//           place.address_components[2].short_name) ||
//           "",
//       ].join(" ");
//     }

//     infowindowContent.children["place-icon"].src = place.icon;
//     infowindowContent.children["place-name"].textContent = place.name;
//     infowindowContent.children["place-address"].textContent = address;
//     infowindow.open(this.map, this.marker2);
//  });
//  }
//  }

onFileSelect(event) {
  if (event.target.files.length > 0) {
    this.fileName = event.target.files[0]['name'];
    const attachments = event.target.files[0];
    this.rsaForm.patchValue({
      attachments: attachments
    });
    this.rsaForm.get('attachments').updateValueAndValidity();
  }
}

 submitForm(): void {
  this.commonService.submitFormWithAttachment('saveItems',this.rsaForm,'attachments').then(data=>
    { 
       this.router.go("/my-account/my-tickets");
       this.toastr.success('Your request has been recorded', 'We will soon assist you!Thank you!');
       
    });
  
  
}




}
