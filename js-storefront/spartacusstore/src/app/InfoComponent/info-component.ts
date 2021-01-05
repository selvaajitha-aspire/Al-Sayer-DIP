import { Component, OnInit } from '@angular/core';
import { CmsComponentData } from '@spartacus/storefront';
import { environment } from '../../environments/environment';

import { ServiceTileBannerComponent } from 'src/app/models/service-tile.model';
import { HomepageInformationComponent } from '../models/info-component.model';

@Component({
  selector: 'app-info-component',
  templateUrl: './info-component.html',
  styleUrls: ['./info-component.scss']
})
export class InfoComponent implements OnInit {


  constructor(public component: CmsComponentData<HomepageInformationComponent>) { }
  ngOnInit(): void {
  }
  getUrl(url: string) {
    if(url && !url.startsWith("/")){
      url = "/"+url;
    }
    return environment.occBaseUrl + url;
  }
  
}



