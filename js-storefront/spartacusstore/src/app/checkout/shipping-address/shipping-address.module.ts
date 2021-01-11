import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CmsConfig, I18nModule, provideDefaultConfig } from '@spartacus/core';

import { ShippingAddressComponent } from './shipping-address.component';
import {  CardModule, SpinnerModule, CheckoutProgressMobileTopModule, CheckoutProgressMobileBottomModule, CheckoutAuthGuard, CartNotEmptyGuard, CheckoutDetailsLoadedGuard } from '@spartacus/storefront';
import { AddressFormModule } from './address-form/address-form.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    AddressFormModule,
    CardModule,
    SpinnerModule,
    I18nModule,
    CheckoutProgressMobileTopModule,
    CheckoutProgressMobileBottomModule,
  ],
  providers: [
    provideDefaultConfig(<CmsConfig>{
      cmsComponents: {
        CheckoutShippingAddress: {
          component: ShippingAddressComponent,
          guards: [
            CheckoutAuthGuard,
            CartNotEmptyGuard,
            CheckoutDetailsLoadedGuard,
          ],
        },
      },
    }),
  ],
  declarations: [ShippingAddressComponent],
  entryComponents: [ShippingAddressComponent],
  exports: [ShippingAddressComponent],
})
export class ShippingAddressModule {}
