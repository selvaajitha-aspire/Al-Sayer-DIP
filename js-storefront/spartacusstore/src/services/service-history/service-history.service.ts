import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ServiceHistoryService {

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(public http:HttpClient, public url:OccEndpointsService) { }

  getServiceHistory(chassisNumber): Observable<any> {
    const api = this.url.getUrl('getServiceHistory') + chassisNumber;
    return this.http.get<any>(api, this.httpOptions).pipe(map(res => res));
  }

}
