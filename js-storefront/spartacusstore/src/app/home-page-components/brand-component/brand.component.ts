import { HttpClient } from '@angular/common/http';
import { Component, OnInit , ChangeDetectorRef } from '@angular/core';
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
  constructor(public component: CmsComponentData<HomepageBannerCollectionComponent>, 
              private registerService: RegisterService,
              private detect: ChangeDetectorRef) { }
  componentData : any[];


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

  ngOnInit(): void {
    this.component.data$.subscribe(data=>{
      this.registerService.getComponentData(data.components).then(res => {
        this.componentData = res.data.component;
        this.detect.detectChanges();
      })
    })
  }
  getUrl(url: string) {
    if(url && !url.startsWith("/")){
      url = "/"+url;
    }
    return environment.occBaseUrl + url;
  }
}
