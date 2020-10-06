import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoadsideAssistanceComponent } from './roadside-assistance.component';
import { FormsModule } from '@angular/forms';
import {ConfigModule, CmsConfig} from '@spartacus/core';
import { rsaOccConfig } from './roadside-assistance-occ-config';



@NgModule({
  declarations: [RoadsideAssistanceComponent],
  imports: [
    CommonModule,
    FormsModule,
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
