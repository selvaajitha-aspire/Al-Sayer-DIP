import { Component, OnInit } from '@angular/core';
import { CmsComponentData } from '@spartacus/storefront';
import { environment } from '../../../environments/environment';

import { ServiceTileBannerComponent } from 'src/app/models/service-tile.model';

@Component({
  selector: 'app-service-tile',
  templateUrl: './service-tile.component.html',
  styleUrls: ['./service-tile.component.scss']
})
export class ServiceTileComponent implements OnInit {


  constructor(public component: CmsComponentData<ServiceTileBannerComponent>) { }
  ngOnInit(): void {
  }
  getUrl(url: string) {
    if(url && !url.startsWith("/")){
      url = "/"+url;
    }
    return environment.occBaseUrl + url;
  }
  getBackgroundColor(color){
    if(color){
      return "linear-gradient(180deg,"+color +", #FFFFFF)";
    }
    else{
      return "";
    }
    
  }
}



