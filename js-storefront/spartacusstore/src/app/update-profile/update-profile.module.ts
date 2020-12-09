import { FormErrorsModule, SpinnerModule } from '@spartacus/storefront';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  AuthGuard,
  CmsConfig,
  I18nModule,
  provideDefaultConfig,
} from '@spartacus/core';
import { UpdateProfileFormComponent } from './components/update-profile-form.component';
import { UpdateProfileComponent } from './update-profile.component';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    I18nModule,
    FormErrorsModule,
    SpinnerModule,
  ],
  providers: [
    provideDefaultConfig(<CmsConfig>{
      cmsComponents: {
        UpdateProfileComponent: {
          component: UpdateProfileComponent,
          guards: [AuthGuard],
        },
      },
    }),
  ],
  declarations: [UpdateProfileComponent, UpdateProfileFormComponent],
  exports: [UpdateProfileComponent, UpdateProfileFormComponent],
  entryComponents: [UpdateProfileComponent],
})
export class UpdateProfileModule {}
