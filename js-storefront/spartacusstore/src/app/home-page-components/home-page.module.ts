import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CmsConfig, ConfigModule, AuthGuard } from '@spartacus/core';
import { BannerComponent } from '@spartacus/storefront';
import { ServiceTileComponent } from './service-tile/service-tile.component';
import { HeaderMenuComponent } from './header/header-menu/header-menu.component';
import { FooterComponent } from './footer/footer.component';
import { BrandComponent } from './brand-component/brand.component';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { RouterModule } from '@angular/router';
@NgModule({
  declarations: [
    ServiceTileComponent,
    BrandComponent
  ],
  imports: [
    CommonModule,
    CarouselModule ,
    RouterModule,
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
        },
        FooterComponent : {
          component: FooterComponent
        },
        HomepageBannerCollectionComponent :{
          component: BrandComponent
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
