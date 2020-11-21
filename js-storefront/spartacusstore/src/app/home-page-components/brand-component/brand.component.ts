import { Component, OnInit } from '@angular/core';
import { CmsComponentData } from '@spartacus/storefront';
import { HomepageBannerComponent } from 'src/app/models/brand.model';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.scss']
})
export class BrandComponent implements OnInit {

  constructor(public component: CmsComponentData<HomepageBannerComponent>) { }

  ngOnInit(): void {
  }
  getUrl(url: string) {
    if(url && !url.startsWith("/")){
      url = "/"+url;
    }
    return environment.occBaseUrl + url;
  }

}
