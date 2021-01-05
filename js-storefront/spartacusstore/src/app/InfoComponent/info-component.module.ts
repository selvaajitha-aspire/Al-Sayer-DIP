import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CmsConfig, ConfigModule, AuthGuard } from '@spartacus/core';
import { InfoComponent } from './info-component';

@NgModule({
  declarations: [
    InfoComponent
  ],
  imports: [
    CommonModule,
    ConfigModule.withConfig({
      cmsComponents: {
        HomepageInformationComponent: {
          component: InfoComponent,
        }
      }
    } as CmsConfig),
  ],
  exports: [
    InfoComponent
  ]
})
export class InfoComponentModule { }
