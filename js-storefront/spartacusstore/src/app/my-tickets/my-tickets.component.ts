import { Component, OnInit, ViewChild } from '@angular/core';
import { MyTicketsService } from 'src/services/my-tickets/my-tickets.service';
import { HeaderTitleService } from './../../services/header-title/header-title.service';
declare var $: any;

@Component({
  selector: 'app-my-tickets',
  templateUrl: './my-tickets.component.html',
  styleUrls: ['./my-tickets.component.scss']
})
export class MyTicketsComponent implements OnInit {
  @ViewChild('map', { static: true }) mapElement: any;
  map: google.maps.Map;
  driverDetails;
  driverName;
  ticketsList;
  currentLatLng;
  driverLatLng;
  marker1:google.maps.Marker;
  marker2:google.maps.Marker;
  ticketToggle = {};
  constructor(protected service:MyTicketsService, private headerTitle : HeaderTitleService) {}

  ngOnInit(): void {
    // this.headerTitle.headerTitle.next('my tickets');
    this.ticketsList=this.service.getRsaRequests() || [];
  }
  
  getDriverLocation(ticket:any){
    const lat=ticket.latitude;
    const lng=ticket.longitude;
    this.driverName=ticket.driverDetails.name;
    const driverLat=ticket.driverDetails.latitude;
    const driverLng=ticket.driverDetails.longitude;
    this.currentLatLng=new google.maps.LatLng(lat,lng);
    const mapProperties = {
      center: this.currentLatLng,
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP
   };
   this.map = new google.maps.Map(this.mapElement.nativeElement, mapProperties);
   
    this.marker1=new google.maps.Marker({
    position:this.currentLatLng,
    map: this.map,
    title: "Your location!"
  }); 
  
  
      const driverLatLng=new google.maps.LatLng(driverLat,driverLng);
      this.driverLatLng=driverLatLng;
      this.marker2=new google.maps.Marker({
      position:driverLatLng,
      map: this.map,
      
      });
      this.calculateAndDisplayRoute();
      $("#locationPopup").modal('show');
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
          const myRoute = response.routes[0].legs[0];
          const n =myRoute.steps.length;
          this.getDuration();
        } else {
          window.alert("Directions request failed due to " + status);
        }
      }
    );
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
          var infowindow = new google.maps.InfoWindow();
          infowindow.setContent("You are currently here.Your driver is "+results[0].distance.text +
          " away. ETA: " +
          results[0].duration.text);
          infowindow.open(this.map,this.marker1)                   
                
        }
      }
    );
  } 

}
