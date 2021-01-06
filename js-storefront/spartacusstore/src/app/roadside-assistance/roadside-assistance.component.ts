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
import { RoutingService, Address } from '@spartacus/core';
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
  imagePath = '';
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
      address:[''],
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
            this.addressU=results[0].formatted_address;
          
           //console.log(results[0].formatted_address)
           this.cd.detectChanges();
            var types=results[0].address_components;
          
            var arrAddress = results[0].address_components;
            console.log(arrAddress);
            
            // iterate through address_component array
            var address:Address={};
            arrAddress.map((address_component)=> {
             
              if (address_component.types[0] == "street_number"){  
                address.line1=address_component.long_name;
            }
                if (address_component.types[0] == "route"){
                  address.line2 = address_component.long_name;
                }
            
                if (address_component.types[0] == "locality"){
                  address.town = address_component.long_name;
                }
            
                if (address_component.types[0] == "administrative_area_level_1"){ 
                  address.region={};
                  address.region.name = address_component.long_name;
                  address.region.isocode=address_component.short_name;
              }

                if (address_component.types[0] == "country"){
                  address.country={};
                  address.country.name = address_component.long_name;
                  address.country.isocode=address_component.short_name;
                }
 
                if (address_component.types[0] == "postal_code"){ 
            
                  address.postalCode = address_component.long_name;
             }
             this.rsaForm.patchValue({
              address: address
            });   
            });
           
          
          } else {
            console.log(('Location not found'));
          }
      } else {
        console.log(('Geocoder failed due to: ' + status));
      }
    }
  )};

  
 
getSelectedVehicle(chassisNumber) {
  this.vehicleList.subscribe(vehicles => {
    vehicles.map(vehicle => {
      if(vehicle.chassisNumber === chassisNumber) {
        if(vehicle.warranties) {
          this.warrantyFlag = false;
          vehicle.warranties.map(warranty => {
            const warrantyTypeFlag = warranty.warrantyType && warranty.warrantyType.includes('14');
            const warrantyDate = warranty.warrantyExpiryDate;
            const brandFlag=vehicle.brand==='LX';
            const warrantyDateFlag = new Date() < new Date(warrantyDate) ? true : false;
            if(warrantyTypeFlag && warrantyDateFlag || brandFlag) {
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



onFileSelect(event) {
  if (event.target.files.length > 0) {
    this.fileName = event.target.files[0]['name'];
    this.imagePath = '';
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

getImage(image) {
  this.imagePath = image;
  this.fileName = 'Captured.png';
}




}
