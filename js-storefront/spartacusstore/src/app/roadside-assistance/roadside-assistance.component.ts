
import { Component, OnInit, ViewChild } from '@angular/core';
import {} from 'googlemaps';
import { RoadsideAssistanceService } from '../../services/roadside-assistance/roadside-assistance.service';
import { NgForm } from '@angular/forms';
import { IssueTypes } from '../models/issue-type.model';



@Component({
  selector: 'app-roadside-assistance',
  templateUrl: './roadside-assistance.component.html',
  styleUrls: ['./roadside-assistance.component.scss']
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
  latitude: any;
  longitude: any;
  user: any;
  vehicleList;
  driverDetails;
  marker1:google.maps.Marker;
  marker2:google.maps.Marker;
  public IssueTypes = IssueTypes;
  directionsService: google.maps.DirectionsService;
  directionsRenderer: google.maps.DirectionsRenderer;
  originIcon = 'https://chart.googleapis.com/chart?' +
            'chst=d_map_pin_letter&chld=O|FFFF00|000000';
 
 // issueTypes: Array<string> = Object.keys(IssueTypes).filter(key => isNaN(+key));
  private autocomplete_init: boolean=false;
  
  constructor(private assistanceService : RoadsideAssistanceService ) { }

  ngOnInit(): void {

    this.vehicleList=this.assistanceService.getVehicles();
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

 getCurrentLocation(){
   this.assistanceService.getPosition().then(pos=>
     {
       console.log(`Positon: ${pos.lng} ${pos.lat}`);
       const currentLatLng=new google.maps.LatLng(pos.lat, pos.lng);
       const mapProperties = {
         center: currentLatLng,
         zoom: 15,
         mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      this.map = new google.maps.Map(this.mapElement.nativeElement, mapProperties);
      this.marker1=new google.maps.Marker({
       position:currentLatLng,
       map: this.map,
       title: "Your current location!",
    
     });
     this.latitude=`${pos.lat}`;
      this.longitude=`${pos.lng}`;
      this.currentLatLng=currentLatLng;
     });
     
 }

 getDriverDetails(){
  this.driverDetails=this.assistanceService.getDriverDetailsPro().then(pos=>
    {
      console.log(`Driver Positon: ${pos.lat} ${pos.lng}`);
      const driverLatLng=new google.maps.LatLng(pos.lng, pos.lat);
      this.driverLatLng=driverLatLng;
      this.marker1=new google.maps.Marker({
        position:driverLatLng,
        map: this.map,
        title: `${pos.name} is here`
      });
      this.calculateAndDisplayRoute();
    })   
  
 }
  
 calculateAndDisplayRoute() {
  console.log(` Positons: ${this.currentLatLng} dest: ${this.driverLatLng}`);
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

autocompleteFocus() {
  alert("Hello Auto")
 this.autocomplete_init = true;
 if (!this.autocomplete_init) {
  
  var input = document.getElementById("latitude") as HTMLInputElement;
  this.autocomplete = new google.maps.places.Autocomplete(input);
   alert(input);
  this.autocomplete.bindTo("bounds", this.map);
  this.autocomplete.setFields(["address_components", "geometry", "icon", "name"]);
  const infowindow = new google.maps.InfoWindow();
  const infowindowContent = document.getElementById(
    "infowindow-content"
  ) as HTMLElement;
  infowindow.setContent(infowindowContent);
  this.autocomplete.addListener("place_changed", () => {
   
   this.marker1.setVisible(false);
   const place = this.autocomplete.getPlace();

   if (!place.geometry) {
     // User entered the name of a Place that was not suggested and
     // pressed the Enter key, or the Place Details request failed.
     window.alert("No details available for input: '" + place.name + "'");
     return;
   }

   // If the place has a geometry, then present it on a map.
   if (place.geometry.viewport) {
     this.map.fitBounds(place.geometry.viewport);
   } else {
     this.map.setCenter(place.geometry.location);
     this.map.setZoom(17);
   }
   this.marker1.setPosition(place.geometry.location);
    this.marker1.setVisible(true);
    let address = "";

    if (place.address_components) {
      address = [
        (place.address_components[0] &&
          place.address_components[0].short_name) ||
          "",
        (place.address_components[1] &&
          place.address_components[1].short_name) ||
          "",
        (place.address_components[2] &&
          place.address_components[2].short_name) ||
          "",
      ].join(" ");
    }

    infowindowContent.children["place-icon"].src = place.icon;
    infowindowContent.children["place-name"].textContent = place.name;
    infowindowContent.children["place-address"].textContent = address;
    infowindow.open(this.map, this.marker1);
 });
 }
 }
  onSubmit(f: NgForm) { 
    f.value.latitude=this.latitude;
    f.value.longitude=this.longitude;
    console.log(f.value); 
    this.assistanceService.storeServiceRequest(f.value);
  }

}
