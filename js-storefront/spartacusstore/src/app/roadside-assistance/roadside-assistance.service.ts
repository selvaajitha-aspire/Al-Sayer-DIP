import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RoadsideAssistanceService {

  private lat: any = '';
  private long: any = '';
  

  public async initialize() {
    await this.getPosition().then(pos => {
      this.lat = pos.lat;
      this.long = pos.lng;
    });
  }
  
  constructor(public http:HttpClient,public url:OccEndpointsService) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  getVehicles(): Observable<any> {
      return this.http.get<any>(this.url.getUrl('getItems'), this.httpOptions).pipe(map((res => res['vehicleList'])));
    }
    
  

  storeServiceRequest(postData:any){
    this.http.post(this.url.getUrl('saveItems'), postData,).toPromise()
    .then(data => {
    });
  }
  

  
  getPosition(): Promise<any>
  {
    return new Promise((resolve, reject) => {

      navigator.geolocation.watchPosition(resp => {

          resolve({lng: resp.coords.longitude, lat: resp.coords.latitude});
        },
        err => {
          reject(err);
        });
    });

  }
}
