import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyVehiclesComponent } from './my-vehicles.component';
import { ConfigModule, CmsConfig } from '@spartacus/core';



@NgModule({
  declarations: [MyVehiclesComponent],
  imports: [
    CommonModule,
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
