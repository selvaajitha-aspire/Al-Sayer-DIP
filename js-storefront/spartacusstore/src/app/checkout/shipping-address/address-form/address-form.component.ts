import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  Address,
  AddressValidation,
  CheckoutDeliveryService,
  Country,
  ErrorModel,
  GlobalMessageService,
  GlobalMessageType,
  Region,
  Title,
  UserAddressService,
  UserService,
} from '@spartacus/core';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { map, switchMap, take, tap } from 'rxjs/operators';


import { ModalRef, ModalService, sortTitles, SuggestedAddressDialogComponent } from '@spartacus/storefront';
import { setFormField } from 'src/app/common/utility';

@Component({
  selector: 'cx-address-form',
  templateUrl: './address-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddressFormComponent implements OnInit, OnDestroy {
  selectedAttributes='';
  countries$: Observable<Country[]>;
  titles$: Observable<Title[]>;
  regions$: Observable<Region[]>;
  selectedCountry$: BehaviorSubject<string> = new BehaviorSubject<string>('');
  addressU='';
  addresses$: Observable<Address[]>;

  @Input()
  addressData: Address;

  @Input()
  actionBtnLabel: string;

  @Input()
  cancelBtnLabel: string;

  @Input()
  setAsDefaultField = true;

  @Input()
  showTitleCode: boolean;

  @Input()
  showCancelBtn = true;

  @Output()
  submitAddress = new EventEmitter<any>();

  @Output()
  backToAddress = new EventEmitter<any>();

  addressVerifySub: Subscription;
  regionsSub: Subscription;
  suggestedAddressModalRef: ModalRef;
 
  addressForm: FormGroup = this.fb.group({
    country: this.fb.group({
      isocode: [null, Validators.required],
    }),
    addressU:[''],
    titleCode: [''],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    line1: ['', Validators.required],
    line2: [''],
    town: ['', Validators.required],
    region: this.fb.group({
      isocode: [null, Validators.required],
    }),
    postalCode: ['', Validators.required],
    phone: '',
    defaultAddress: [false],
  });

  constructor(
    protected fb: FormBuilder,
    protected checkoutDeliveryService: CheckoutDeliveryService,
    protected userService: UserService,
    protected userAddressService: UserAddressService,
    protected globalMessageService: GlobalMessageService,
    protected modalService: ModalService
  ) {}

  ngOnInit() {
    // Fetching countries
    
    this.countries$ = this.userAddressService.getDeliveryCountries().pipe(
      tap((countries: Country[]) => {
        if (Object.keys(countries).length === 0) {
          this.userAddressService.loadDeliveryCountries();
        }
      })
    );
     this.countries$.subscribe(countries=>{
       countries.map(country=>{
         this.selectedAttributes=country.isocode;
         this.selectedCountry$.next(country.isocode);
         return 
       })
     })

    // Fetching titles
    this.titles$ = this.userService.getTitles().pipe(
      tap((titles: Title[]) => {
        if (Object.keys(titles).length === 0) {
          this.userService.loadTitles();
        }
      }),
      map((titles) => {
        titles.sort(sortTitles);
        const noneTitle = { code: '', name: 'Title' };
        return [noneTitle, ...titles];
      })
    );

    // Fetching regions
    this.regions$ = this.selectedCountry$.pipe(
      switchMap((country) => this.userAddressService.getRegions(country)),
      tap((regions: Region[]) => {
        const regionControl = this.addressForm.get('region.isocode');
        if (regions && regions.length > 0) {
          regionControl.enable();
        } else {
          regionControl.disable();
        }
      })
    );

    // verify the new added address
    this.addressVerifySub = this.checkoutDeliveryService
      .getAddressVerificationResults()
      .subscribe((results: AddressValidation) => {
        if (results.decision === 'FAIL') {
          this.checkoutDeliveryService.clearAddressVerificationResults();
        } else if (results.decision === 'ACCEPT') {
          this.submitAddress.emit(this.addressForm.value);
        } else if (results.decision === 'REJECT') {
          // TODO: Workaround: allow server for decide is titleCode mandatory (if yes, provide personalized message)
          if (
            results.errors.errors.some(
              (error: ErrorModel) => error.subject === 'titleCode'
            )
          ) {
            this.globalMessageService.add(
              { key: 'addressForm.titleRequired' },
              GlobalMessageType.MSG_TYPE_ERROR
            );
          } else {
            this.globalMessageService.add(
              { key: 'addressForm.invalidAddress' },
              GlobalMessageType.MSG_TYPE_ERROR
            );
          }
          this.checkoutDeliveryService.clearAddressVerificationResults();
        } else if (results.decision === 'REVIEW') {
          this.openSuggestedAddress(results);
        }
      });

    if (this.addressData && Object.keys(this.addressData).length !== 0) {
      this.addressForm.patchValue(this.addressData);

      this.countrySelected(this.addressData.country);
      if (this.addressData.region) {
        this.regionSelected(this.addressData.region);
      }
    }

    this.addresses$ = this.userAddressService.getAddresses();
  }

  countrySelected(country: Country): void {
    this.addressForm['controls'].country['controls'].isocode.setValue(
      country.isocode
    );
    this.selectedCountry$.next(country.isocode);
  }

  regionSelected(region: Region): void {
    this.addressForm['controls'].region['controls'].isocode.setValue(
      region.isocode
    );
  }

  toggleDefaultAddress(): void {
    this.addressForm['controls'].defaultAddress.setValue(
      this.addressForm.value.defaultAddress
    );
  }

  back(): void {
    this.backToAddress.emit();
  }

  verifyAddress(): void {
    console.log(this.addressForm.value);
    if (this.addressForm.valid) {
      
      if (this.addressForm.get('region').value.isocode) {
        this.regionsSub = this.regions$.pipe(take(1)).subscribe((regions) => {
          const obj = regions.find(
            (region) =>
              region.isocode ===
              this.addressForm.controls['region'].value.isocode
          );
          Object.assign(this.addressForm.value.region, {
            isocodeShort: obj.isocodeShort,
          });
        });
      }

      if (this.addressForm.dirty) {
        this.checkoutDeliveryService.verifyAddress(this.addressForm.value);
      } else {
        // address form value not changed
        // ignore duplicate address
        this.submitAddress.emit(undefined);
      }
    } else {
      this.addressForm.markAllAsTouched();
    }
  }

  openSuggestedAddress(results: AddressValidation): void {
    if (!this.suggestedAddressModalRef) {
      this.suggestedAddressModalRef = this.modalService.open(
        SuggestedAddressDialogComponent,
        { centered: true, size: 'lg' }
      );
      this.suggestedAddressModalRef.componentInstance.enteredAddress = this.addressForm.value;
      this.suggestedAddressModalRef.componentInstance.suggestedAddresses =
        results.suggestedAddresses;
      this.suggestedAddressModalRef.result
        .then((address) => {
          this.checkoutDeliveryService.clearAddressVerificationResults();
          if (address) {
            address = Object.assign(
              {
                titleCode: this.addressForm.value.titleCode,
                phone: this.addressForm.value.phone,
                selected: true,
              },
              address
            );
            this.submitAddress.emit(address);
          }
          this.suggestedAddressModalRef = null;
        })
        .catch(() => {
          // this  callback is called when modal is closed with Esc key or clicking backdrop
          this.checkoutDeliveryService.clearAddressVerificationResults();
          const address = Object.assign(
            {
              selected: true,
            },
            this.addressForm.value
          );
          this.submitAddress.emit(address);
          this.suggestedAddressModalRef = null;
        });
    }
  }
  setAddress(addressObject) {
    this.addressForm.reset();
    this.addressForm.get('addressU').patchValue(addressObject.address)
    console.log(addressObject);
    var arrAddress:google.maps.GeocoderAddressComponent[] = addressObject.addressComponent;
           
            arrAddress.map((address_component)=> {
             
                if (address_component.types[0] == "establishment"){  
                setFormField(this.addressForm,"line1",address_component.long_name);
                console.log("building"+address_component.long_name)
                }
                if (address_component.types[0] == "route"){  
                  setFormField(this.addressForm,"line1",address_component.long_name);
                  console.log("building"+address_component.long_name)
                }
                if (address_component.types[0] == "political"){
                  setFormField(this.addressForm,"line2",address_component.long_name);
                }
            
                if (address_component.types[0] == "administrative_area_level_2"){
                  setFormField(this.addressForm,"town",address_component.long_name);
                  console.log("city"+address_component.long_name)
                }
                if (address_component.types[0] == "locality"){
                  setFormField(this.addressForm,"town",address_component.long_name);
                  console.log("city"+address_component.long_name)
                }
            
                if (address_component.types[0] == "administrative_area_level_1"){ 
                  setFormField(this.addressForm,"region.isocode",address_component.long_name);
              }
 
                if (address_component.types[0] == "postal_code"){ 
                  setFormField(this.addressForm,"postalCode",address_component.long_name);
             }
          });
  }

  ngOnDestroy() {
    this.checkoutDeliveryService.clearAddressVerificationResults();

    if (this.addressVerifySub) {
      this.addressVerifySub.unsubscribe();
    }

    if (this.regionsSub) {
      this.regionsSub.unsubscribe();
    }
  }

  
}
