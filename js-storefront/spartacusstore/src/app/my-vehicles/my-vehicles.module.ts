import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyVehiclesComponent } from './my-vehicles.component';
import { ConfigModule, CmsConfig, I18nModule } from '@spartacus/core';



@NgModule({
  declarations: [MyVehiclesComponent],
  imports: [
    CommonModule,
    I18nModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        MyVehiclesComponent: {
          component:MyVehiclesComponent ,
        },
      },
    }),
  ]
})
export class MyVehiclesModule { }
