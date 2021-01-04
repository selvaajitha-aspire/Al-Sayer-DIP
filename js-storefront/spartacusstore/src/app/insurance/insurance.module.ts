import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InsuranceComponent } from './insurance.component';
import { ConfigModule, CmsConfig } from '@spartacus/core';



@NgModule({
  declarations: [InsuranceComponent],
  imports: [
    CommonModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        MyPurchasesComponent: {
          component:InsuranceComponent ,
        },
      },
    }),
  ]
})
export class InsuranceModule { }
