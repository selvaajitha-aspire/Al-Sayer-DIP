import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoadsideAssistanceComponent } from './roadside-assistance.component';
import { FormsModule } from '@angular/forms';
import {ConfigModule, CmsConfig} from '@spartacus/core';
import { rsaOccConfig } from '../config/roadside-assistance-occ-config';
import { ReactiveFormsModule } from '@angular/forms';
import { FormErrorsModule } from '@spartacus/storefront';



@NgModule({
  declarations: [RoadsideAssistanceComponent],
  imports: [
    CommonModule,
    FormsModule,
    FormErrorsModule,
    ReactiveFormsModule,
    ConfigModule.withConfig(rsaOccConfig),
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        RoadsideAssistanceComponent: {
          component:RoadsideAssistanceComponent ,
        },
      },
    }),
  ],
})
export class RoadsideAssistanceModule { }
