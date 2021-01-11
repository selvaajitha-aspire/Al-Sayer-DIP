import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceTypeComponent } from './service-type.component';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { UseMyLocationComponent } from 'src/app/use-my-location/use-my-location.component';



@NgModule({
  declarations: [ServiceTypeComponent],
  imports: [
    CommonModule,
    I18nModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        CheckoutServiceTypeComponent: {
          component:ServiceTypeComponent ,
        },
      },
    }),
  ],
  // exports : [ServiceTypeComponent,UseMyLocationComponent],
  //  entryComponents : [UseMyLocationComponent],
})
export class ServiceTypeModule { }
