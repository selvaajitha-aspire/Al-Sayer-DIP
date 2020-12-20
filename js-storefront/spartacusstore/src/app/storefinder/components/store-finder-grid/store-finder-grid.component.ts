import { Component, OnDestroy, OnInit, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StoreFinderService, GeoPoint, StoreDataService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'cx-store-finder-grid',
  templateUrl: './store-finder-grid.component.html',
  styleUrls: ['./store-finder-grid.component.scss']
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
  activeLocationName = '';
  markers=Array();
  infoWindows=Array();

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
    for (let i = 0; i < this.locations.length; i++) {
      const latitude = this.storeDataService.getStoreLatitude(this.locations[i]);
    const longitude = this.storeDataService.getStoreLongitude(this.locations[i]);
    const marker=  new google.maps.Marker({
        position: { lat: latitude, lng: longitude },
        map,
        title: this.locations[i]['name'],
        
      });
      this.markers.push(marker);
      let closedDays = "";
      let openingDays = Array();
      let openingTime = "";
      let closingTime = "";
      let weekDayOpeningListNew : [] = this.locations[i]['openingHours']['weekDayOpeningList'];
      for (let j=0; j < weekDayOpeningListNew.length; j++) {
        if(weekDayOpeningListNew[j]['closed']) {
          closedDays += weekDayOpeningListNew[j]['weekDay'] + ' ';
        } else {
          openingDays.push(weekDayOpeningListNew[j]['weekDay']);
          openingTime = weekDayOpeningListNew[j]['openingTime']['formattedHour'] ;
          closingTime = weekDayOpeningListNew[j]['closingTime']['formattedHour'];
        }
      }
      let contentString = '<p class="location-name">'+ this.locations[i]['name'] + '</p>'+
      '<div class="timing-wrapper"><div class="timing-text">Timing</div>' + 
      '<div class="timings">' + openingDays[0] + '- '+ openingDays[openingDays.length -1] + ' : ' + openingTime +' - '+ closingTime +'</div>' +
      '<div class="timings">'+ closedDays  +': Closed</div></div>'+
      '<div class="telephone-wrapper"><div class="telephone-text">Telephone</div><div class="telephone">1803803</div></div>';
     const infoWindow= new google.maps.InfoWindow({
        content : contentString
        
    });
    this.infoWindows.push(infoWindow);
    this.onClickEvent(this.markers,map,this.infoWindows);
    
    }
  }

  onClickEvent(markers,map,infoWindows){
    for (let i = 0; i < markers.length; i++){
     
      google.maps.event.addListener(markers[i], 'click', function() {
        infoWindows[i].open(map,markers[i] );
      });
    }
  }

  loadActiveLocation(location) {
    let index = location &&  location.locationIndex;
    this.activeLocationName = location && location.location.displayName;
    this.infoWindows[index].open(this.map, this.markers[index] );
  }

  ngOnDestroy() {}
}
