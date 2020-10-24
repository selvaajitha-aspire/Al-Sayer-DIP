import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CmsConfig, ConfigModule, AuthGuard } from '@spartacus/core';
import { BannerComponent } from '@spartacus/storefront';
import { ServiceTileComponent } from './service-tile/service-tile.component';
import { HeaderMenuComponent } from './header/header-menu/header-menu.component';

@NgModule({
  declarations: [
    ServiceTileComponent
  ],
  imports: [
    CommonModule,
    ConfigModule.withConfig({
      cmsComponents: {
        ServiceTileBannerComponent: {
          component: ServiceTileComponent,
        },
        NavigationComponent: {
          component: HeaderMenuComponent
        },
        PreHeader: {
          component: HeaderMenuComponent
        }
      }
    } as CmsConfig),
  ],
  exports: [
    ServiceTileComponent
  ],
  providers: [
    BannerComponent
  ]
})
export class HomePageModule { }
