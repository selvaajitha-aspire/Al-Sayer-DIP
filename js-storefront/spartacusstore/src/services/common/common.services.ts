import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class CommonService {

  constructor(public http:HttpClient,public url:OccEndpointsService) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };


    get(strUrl): Observable<any> {
      return this.http.get<any>(this.url.getUrl(strUrl), this.httpOptions);
    }
    
    getWithRestParam(strUrl,...params): Observable<any> {
        return this.http.get<any>(this.url.getUrl(strUrl), this.httpOptions);
    }

    getParamString(params){
        var str ="";
        params.map(param=>{
          str = str + '/' + param 
        });
        return str;
    }

    postRequest(strUrl,postData:FormData,...params): Promise<any>{
        // const attachments=postData.get("attachments").valueOf();
        return new Promise((resolve,reject)=>{
         let url = this.url.getUrl(strUrl) + this.getParamString(params);
         this.http.post(url,postData,this.httpOptions).toPromise().then( 
           data=>{
             resolve({success:"true",data:data})
            },
         err => {
           reject(err);
         }); 
       });
     }

     postMultipartRequest(strUrl,postData:FormData,...params): Promise<any>{
      // const attachments=postData.get("attachments").valueOf();
      return new Promise((resolve,reject)=>{
       let url = this.url.getUrl(strUrl) + this.getParamString(params);
       this.http.post(url,postData).toPromise().then( 
         data=>{
           resolve({success:"true",data:data})
          },
       err => {
         reject(err);
       }); 
     });
   }

    submitForm(strUrl,formObject,...attachments){
        debugger;
        if(formObject.valid){
            this.postRequest(strUrl,formObject.value);      
        }
    }

    submitFormWithAttacment(strUrl,formObject,...attachments){
      debugger;
      if(formObject.valid){
          const formData = new FormData();
          
          if(null != attachments && attachments.length>0){
              this.appendAttachmentsToFormData(formData,attachments,formObject);   
          }   

          formData.append( 'form', new Blob([JSON.stringify(formObject.value)],{type:'application/json'})  );

          this.postMultipartRequest(strUrl,formData);      
      }
    }

    appendAttachmentsToFormData(formData,attachmentList,formObject){
        attachmentList.map(att=>{
            formData.append(att, formObject.get(att).value);
        });
        return formData;
    }

  

}
