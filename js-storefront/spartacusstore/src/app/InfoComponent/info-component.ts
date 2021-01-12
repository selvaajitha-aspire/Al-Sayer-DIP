import { Component, OnInit, AfterViewInit, Inject } from '@angular/core';
import { CmsComponentData } from '@spartacus/storefront';
import { environment } from '../../environments/environment';
import { DOCUMENT } from '@angular/common';

import { ServiceTileBannerComponent } from 'src/app/models/service-tile.model';
import { HomepageInformationComponent } from '../models/info-component.model';

@Component({
  selector: 'app-info-component',
  templateUrl: './info-component.html',
  styleUrls: ['./info-component.scss']
})
export class InfoComponent implements OnInit, AfterViewInit {
  expandFlag : boolean = false;

  constructor(public component: CmsComponentData<HomepageInformationComponent>, @Inject(DOCUMENT) public document: Document) { }
  ngOnInit(): void {  }

  ngAfterViewInit() {
    if(Array.from(document.getElementsByClassName('Section1') as HTMLCollectionOf<HTMLElement>)) {
      const elements = Array.from(document.getElementsByClassName('Section1') as HTMLCollectionOf<HTMLElement>);
      elements.forEach((element) => {
        element.style.display = 'block';
    }); 
    }
  }

  getUrl(url: string) {
    if(url && !url.startsWith("/")){
      url = "/"+url;
    }
    return environment.occBaseUrl + url;
  }
  
}



