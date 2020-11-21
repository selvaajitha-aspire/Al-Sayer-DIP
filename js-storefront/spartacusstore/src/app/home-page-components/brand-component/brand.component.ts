import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CmsComponentData } from '@spartacus/storefront';
import { OwlOptions } from 'ngx-owl-carousel-o';
import { throwError } from 'rxjs';
import { HomepageBannerCollectionComponent } from 'src/app/models/brand-list.model';
import { HomepageBannerComponent } from 'src/app/models/brand.model';
import { environment } from 'src/environments/environment';
import { RegisterService } from 'src/services/register/register.service';
export interface PhotosApi {
  albumId?: number;
  id?: number;
  title?: string;
  url?: string;
  thumbnailUrl?: string;
}
@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.scss']
})
export class BrandComponent implements OnInit {
  componentData : any[];
  constructor(public component: CmsComponentData<HomepageBannerCollectionComponent>,private readonly http: HttpClient, private registerService: RegisterService) { }

  ngOnInit(): void {
    this.component.data$.subscribe(data=>{
      this.registerService.getComponentData(data.components).then(res => {
        console.log(res.data.component);
        this.componentData = res.data.component;
      })
    })
  }
  getUrl(url: string) {
    if(url && !url.startsWith("/")){
      url = "/"+url;
    }
    return environment.occBaseUrl + url;
  }


  apiData: PhotosApi;
  limit: number = 10; // <==== Edit this number to limit API results
  customOptions: OwlOptions = {
    loop: true,
    autoplay: true,
    center: true,
    dots: true,
    autoHeight: true,
    autoWidth: true,
    responsive: {
      0: {
        items: 1,
      },
      600: {
        items: 1,
      },
      1000: {
        items: 1,
      }
    }
  }
  


  fetch() {
    const api = `https://jsonplaceholder.typicode.com/albums/1/photos?_start=0&_limit=${this.limit}`;
    const http$ = this.http.get<PhotosApi>(api);

    http$.subscribe(
      res => this.apiData = res,
      err => throwError(err)
    )
  }

  


 

}
