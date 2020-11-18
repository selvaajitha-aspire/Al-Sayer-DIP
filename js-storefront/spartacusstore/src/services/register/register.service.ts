
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
    let url = this.url.getUrl('sendOTP') + civilId;
    this.http.post(url, mobile, this.httpOptions).toPromise()
    .then(data => {
    });
  }


  getECCCustomerDetails(civilId): Promise<any> 
  {  
    return new Promise((resolve,reject)=>{
      let url = this.url.getUrl('getEccCustomer')+civilId;
      this.http.get<any>(url,this.httpOptions).toPromise().then((res:any) =>{
          resolve({civilId: res.civilId,eccCustId: res.eccCustId,name: res.name, arabicName: res.arabicName,mobile : res.mobile})
      },
      err => {
        reject(err);
      }); 
    });
  }

  register(registerData): Promise<any> {
    return new Promise((resolve,reject)=>{
    this.http.post(this.url.getUrl('registerCustomer'), registerData).toPromise().then((res:any)=>{
      resolve({status:"success"})
    },
    err=>{
      reject(err);
    });
  });
}
}



