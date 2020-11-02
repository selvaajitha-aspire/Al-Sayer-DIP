
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './register.component';
import { provideDefaultConfig, CmsConfig, NotAuthGuard, ConfigModule, AuthModule, provideConfigFromMetaTags } from '@spartacus/core';
import { RouterModule } from '@angular/router';
import { RecaptchaModule } from 'ng-recaptcha';
import { FormErrorsModule } from '@spartacus/storefront';
import { registerOccConfig } from '../config/register-config';




@NgModule({
  declarations: [RegisterComponent],
  imports: [
    CommonModule,
   FormsModule,
   ReactiveFormsModule,
   RouterModule,
   FormErrorsModule,
   RecaptchaModule,
  // AuthModule.forRoot(),
   ConfigModule.withConfig(registerOccConfig),
  ],
  providers: [
    provideDefaultConfig(<CmsConfig>{
      cmsComponents: {
        RegisterCustomerComponent: {
          component: RegisterComponent,
          guards: [NotAuthGuard],
        },
      },
    }),
    ...provideConfigFromMetaTags(),
  ],
  exports: [RegisterComponent],
  entryComponents: [RegisterComponent],
})
export class RegisterModule {
 }
