
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';


@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(public http:HttpClient,public url:OccEndpointsService) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
    }) 
  };
  
  sendOtpOnMobile(mobile,civilId){
    let params = new HttpParams().set("id",civilId);
    
    this.http.post(this.url.getUrl('sendOTP',{params: params}),mobile,this.httpOptions).toPromise()
    .then(data => {
    });
  }


  getECCCustomerDetails(civilId): Promise<any> 
  {  
    let params = new HttpParams().set("id",civilId);
    return new Promise((resolve,reject)=>{
      this.http.get<any>(this.url.getUrl('getEccCustomer',{params: params}),this.httpOptions).toPromise().then((res:any) =>{
          resolve({civilId: res.civilId,eccCustId: res.eccCustId,name: res.name, arabicName: res.arabicName,mobile : res.mobile})
      },
      err => {
        reject(err);
      }); 
    });
  }

  register(registerData){
    this.http.post(this.url.getUrl('registerCustomer'), registerData).toPromise()
    .then(data => {
    });
  }
}



