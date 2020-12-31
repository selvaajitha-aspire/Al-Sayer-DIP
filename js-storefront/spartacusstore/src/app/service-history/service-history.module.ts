import { registerOccConfig } from './../config/register-config';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceHistoryComponent } from './service-history.component';
import { ConfigModule, CmsConfig, I18nModule } from '@spartacus/core';


@NgModule({
  declarations: [ServiceHistoryComponent],
  imports: [
    CommonModule,
    I18nModule,
    ConfigModule.withConfig(registerOccConfig),
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        ServiceHistoryComponent: {
          component:ServiceHistoryComponent ,
        },
      },
    }),
  ]
})
export class ServiceHistoryModule { }
