import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoadsideAssistanceComponent } from './roadside-assistance.component';
import { FormsModule } from '@angular/forms';
import {ConfigModule, CmsConfig, AuthGuard, I18nModule} from '@spartacus/core';
import { rsaOccConfig } from '../config/roadside-assistance-occ-config';
import { ReactiveFormsModule } from '@angular/forms';
import { FormErrorsModule } from '@spartacus/storefront';

import { RouterModule, Routes } from '@angular/router';
import { CmsPageGuard } from '@spartacus/storefront';
import { CameraComponent } from '../common/CameraComponent/camera.component';
import { MarkAsteriskDirectiveModule } from '../directives/mark-asterisk.directive';
const staticRoutes: Routes = [{
  path: 'my-services', children: [
    {
      path: "roadsideassistance",
      component: RoadsideAssistanceComponent
    }
    
  ],
  canActivate : [CmsPageGuard, AuthGuard]
}];


@NgModule({
  declarations: [RoadsideAssistanceComponent,CameraComponent],
  imports: [
    CommonModule,
    FormsModule,
    FormErrorsModule,
    ReactiveFormsModule,
    MarkAsteriskDirectiveModule,
    I18nModule,
    ConfigModule.withConfig(rsaOccConfig),
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        RoadsideAssistanceComponent: {
          component:RoadsideAssistanceComponent ,
        },
      },
    }),
    RouterModule.forChild(staticRoutes)
  ],
  exports : [CameraComponent],
  entryComponents : [CameraComponent],
})
export class RoadsideAssistanceModule { }
