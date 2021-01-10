import { UseMyLocationService } from './use-my-location.service';
import { Component, OnInit, ViewChild, NgZone, ChangeDetectorRef } from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-use-my-location',
  templateUrl: './use-my-location.component.html',
  styleUrls: ['./use-my-location.component.scss']
})

export class UseMyLocationComponent implements OnInit {
  @ViewChild('map', { static: true }) mapElement: any;
  map: google.maps.Map;
  private lat: any = 29.378586 ;
  private lng: any = 47.990341;
  latLng=new google.maps.LatLng(this.lat,this.lng);
  currentLatLng;
  addressU='';
  marker1:google.maps.Marker;
  marker2:google.maps.Marker;
  
  ReqJson: any = {};
  directionsService: google.maps.DirectionsService;
  directionsRenderer: google.maps.DirectionsRenderer;
  originIcon = 'https://chart.googleapis.com/chart?' +
            'chst=d_map_pin_letter&chld=O|FFFF00|000000';
  geocoder = new google.maps.Geocoder();
  
  constructor(private useMyLocationService : UseMyLocationService,private ngZone: NgZone,private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
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
   
    this.useMyLocationService.getPosition().then(pos=>
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
       
       this.currentLatLng=currentLatLng;
       this.getAddress();
       google.maps.event.addListener(marker1, "dragend",  (mEvent)=>{
          this.ngZone.run(() => {
           
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
           } else {
             console.log(('Location not found'));
           }
       } else {
         console.log(('Geocoder failed due to: ' + status));
       }
     }
   )};
}
