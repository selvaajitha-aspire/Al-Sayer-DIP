import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { I18nModule } from '@spartacus/core';

import { AddressFormComponent } from './address-form.component';
import { SuggestedAddressDialogComponent } from './suggested-addresses-dialog/suggested-addresses-dialog.component';
import { IconModule, FormErrorsModule } from '@spartacus/storefront';
import { UseMyLocationComponent } from 'src/app/use-my-location/use-my-location.component';


@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule,
    NgSelectModule,
    IconModule,
    I18nModule,
    FormErrorsModule,
  ],
  declarations: [AddressFormComponent, SuggestedAddressDialogComponent,UseMyLocationComponent],
  entryComponents: [SuggestedAddressDialogComponent,UseMyLocationComponent],
  exports: [AddressFormComponent, SuggestedAddressDialogComponent,UseMyLocationComponent],
})
export class AddressFormModule {}
