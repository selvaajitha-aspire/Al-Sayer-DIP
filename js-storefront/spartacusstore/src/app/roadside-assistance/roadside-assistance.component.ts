
import { Component, OnInit, ViewChild } from '@angular/core';
import {} from 'googlemaps';
import { RoadsideAssistanceService } from './roadside-assistance.service';
import { NgForm } from '@angular/forms';
import { IssueTypes } from './issue-type-enum'


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
  autocomplete:google.maps.places.Autocomplete;
  latitude: any;
  longitude: any;
  user: any;
  vehicleList;
  marker:google.maps.Marker;
  public IssueTypes = IssueTypes;
 
 // issueTypes: Array<string> = Object.keys(IssueTypes).filter(key => isNaN(+key));
  private autocomplete_init: boolean=true;
  
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
     title: "Static Location"
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
      new google.maps.Marker({
       position:currentLatLng,
       map: this.map,
       title: "Your current location!"
     });
     this.latitude=`${pos.lat}`;
      this.longitude=`${pos.lng}`;
     });
     
 }
  

autocompleteFocus() {
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
   
   this.marker.setVisible(false);
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
   this.marker.setPosition(place.geometry.location);
    this.marker.setVisible(true);
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
    infowindow.open(this.map, this.marker);
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
