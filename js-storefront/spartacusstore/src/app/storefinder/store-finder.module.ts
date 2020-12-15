import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbTabsetModule } from '@ng-bootstrap/ng-bootstrap';
import {
  CmsConfig,
  I18nModule,
  provideDefaultConfig,
  StoreFinderCoreModule,
  UrlModule,
} from '@spartacus/core';

import { ScheduleComponent } from './components/schedule-component/schedule.component';
import { StoreFinderGridComponent } from './components/store-finder-grid/store-finder-grid.component';
import { StoreFinderListItemComponent } from './components/store-finder-list-item/store-finder-list-item.component';
import { StoreFinderMapComponent } from './components/store-finder-map/store-finder-map.component';
import { StoreFinderListComponent } from './components/store-finder-search-result/store-finder-list/store-finder-list.component';
import { StoreFinderSearchResultComponent } from './components/store-finder-search-result/store-finder-search-result.component';

import { StoreFinderComponent } from './components/store-finder/store-finder.component';
import { ListNavigationModule, SpinnerModule, LayoutConfig } from '@spartacus/storefront';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    ListNavigationModule,
    NgbTabsetModule,
    SpinnerModule,
    UrlModule,
    StoreFinderCoreModule,
    I18nModule,
    
  ],
  providers: [
    provideDefaultConfig(<CmsConfig | LayoutConfig>{
      cmsComponents: {
        StoreFinderComponent: {
          component: StoreFinderGridComponent,
          
        },
      },
      layoutSlots: {
        StoreFinderPageTemplate: {
          slots: ['MiddleContent', 'SideContent'],
        },
      },
    }),
  ],
  declarations: [
    
    StoreFinderListComponent,
    StoreFinderMapComponent,
    StoreFinderListItemComponent,
    StoreFinderGridComponent,
    ScheduleComponent,
    StoreFinderSearchResultComponent,
    StoreFinderComponent,
  ],
  exports: [
    ScheduleComponent,
    StoreFinderComponent,
    StoreFinderGridComponent,
    StoreFinderListItemComponent,
    StoreFinderMapComponent,
    StoreFinderSearchResultComponent,
    StoreFinderListComponent,
    
    
  ],
  entryComponents: [
    StoreFinderComponent,
    StoreFinderSearchResultComponent,
    StoreFinderGridComponent,
    
  ],
})
export class StoreFinderModule {}
