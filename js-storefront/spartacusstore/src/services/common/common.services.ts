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



    private get(strUrl): Observable<any> {
      return this.http.get<any>(strUrl, this.httpOptions);
    }
    
    getWithParam(component,responseHandler,strUrl,...params){
        let str = this.url.getUrl(strUrl) + this.getParamString(params);
        this.get(str).subscribe(resp=>{
            component[responseHandler](resp);
        });
    }

    getObservableWithParam(component,responseHandler,strUrl,respParam,...params){
      let str = this.url.getUrl(strUrl) + this.getParamString(params);
      var resp = this.get(str).pipe(map(res=>res[respParam]));
      component[responseHandler](resp);
    }

    postWithParam(component,responseHandler,strUrl,postData,...params){
      let str = this.url.getUrl(strUrl) + this.getParamString(params);
      this.http.post(str,postData).subscribe(resp=>{
          component[responseHandler](resp);
      });
    }

    postObservableWithParam(component,responseHandler,respParam,strUrl,postData,...params){
      let str = this.url.getUrl(strUrl) + this.getParamString(params);
      var resp = this.http.post(str,postData).pipe(map(res=>res[respParam]));
      component[responseHandler](resp);
    }
    
    private getParamString(params){
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

    submitForm(strUrl,formObject){
        if(formObject.valid){
           return this.postRequest(strUrl,formObject.value);     
        }
        else {
          formObject.markAllAsTouched();
        }
        
    }

    submitFormWithAttachment(strUrl,formObject,...attachments){
      if(formObject.valid){
          const formData = new FormData();
          
          if(null != attachments && attachments.length>0){
              this.appendAttachmentsToFormData(formData,attachments,formObject);   
          }   

          formData.append( 'form', new Blob([JSON.stringify(formObject.value)],{type:'application/json'})  );

          return this.postMultipartRequest(strUrl,formData);      
      }
      else {
        formObject.markAllAsTouched();
      }
    }

    appendAttachmentsToFormData(formData,attachmentList,formObject){
        attachmentList.map(att=>{
            formData.append(att, formObject.get(att).value);
        });
        return formData;
    }

    submitAttachment(strUrl,formObject,...attachments){
      if(formObject.valid){
          const formData = new FormData();
          
          if(null != attachments && attachments.length>0){
              this.appendAttachmentsToFormData(formData,attachments,formObject);   
          }   
          
          return this.postMultipartRequest(strUrl,formData);      
      }
      else {
        formObject.markAllAsTouched();
      }
    }

}
