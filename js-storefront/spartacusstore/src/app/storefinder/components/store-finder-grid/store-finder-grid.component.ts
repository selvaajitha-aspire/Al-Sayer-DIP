import { Component, OnDestroy, OnInit, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StoreFinderService, GeoPoint, StoreDataService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'cx-store-finder-grid',
  templateUrl: './store-finder-grid.component.html',
})
export class StoreFinderGridComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('map', { static: true }) mapElement:ElementRef;
  map: google.maps.Map;
  locations$: any;
  isLoading$: Observable<boolean>;
  defaultLocation: GeoPoint;
  country: string;
  region: string;
  locations:[];

  constructor(
    private storeFinderService: StoreFinderService,
    protected storeDataService: StoreDataService,
    private route: ActivatedRoute
  ) {}
  ngAfterViewInit(): void {
    console.log(this.mapElement);
    const mapProperties = {
      center:{ lat: 29.083715, lng: 48.089963 },
      zoom: 10,
      mapTypeId: google.maps.MapTypeId.ROADMAP
   };
   
   this.map = new google.maps.Map(this.mapElement.nativeElement, mapProperties);
   this.locations$.subscribe(locations=>{
    this.locations= locations && locations.stores;
    
    this.locations  && this.setMarkers(this.map)
    
    
  })
   
  
  }

  ngOnInit() {
    this.isLoading$ = this.storeFinderService.getViewAllStoresLoading();
    this.locations$ = this.storeFinderService.getViewAllStoresEntities();
   
    this.defaultLocation = {};

    
      this.storeFinderService.findStoresAction(
        '',
        {
          pageSize: -1,
        },
        undefined,
        'KW'
      );

      
       
       
    
  }
  setMarkers(map: google.maps.Map) {
let markers=Array();
let infoWindows=Array();
  console.log(this.locations)
    for (let i = 0; i < this.locations.length; i++) {
      const latitude = this.storeDataService.getStoreLatitude(this.locations[i]);
    const longitude = this.storeDataService.getStoreLongitude(this.locations[i]);
      console.log("LATLONG"+latitude+longitude);
    const marker=  new google.maps.Marker({
        position: { lat: latitude, lng: longitude },
        map,
        title: this.locations[i]['name'],
        
      });
      markers.push(marker);
     const infoWindow= new google.maps.InfoWindow({
        content : this.locations[i]['name']
        
    });
    infoWindows.push(infoWindow);
    this.onClickEvent(markers,map,infoWindows);
    
    }
  }

  onClickEvent(markers,map,infoWindows){
    for (let i = 0; i < markers.length; i++){
     
      google.maps.event.addListener(markers[i], 'click', function() {
        infoWindows[i].open(map,markers[i] );
      });
    }
  }

  ngOnDestroy() {}
}
