import { ToastrService } from 'ngx-toastr';
import { RegisterService } from '../../services/register/register.service';
import { UserRegister } from './../models/user-register.model';
import { RecaptchaService } from '../../services/recaptcha/recaptcha.service';
import { Component, OnDestroy, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
  NgForm,
} from '@angular/forms';
import {
  AnonymousConsent,
  AnonymousConsentsConfig,
  AnonymousConsentsService,
  AuthRedirectService,
  AuthService,
  ConsentTemplate,
  GlobalMessageEntities,
  GlobalMessageService,
  GlobalMessageType,
  RoutingService,
  Title,
  UserService,
} from '@spartacus/core';
import { combineLatest, Observable, Subscription } from 'rxjs';
import { filter, map, tap } from 'rxjs/operators';
import { CustomFormValidators, sortTitles } from '@spartacus/storefront';
import { CommonService } from 'src/services/common/common.services';
import { setFormField, isEmpty } from '../common/utility';

declare var $: any;
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {
  sub: Subscription;
  @ViewChild('registrationForm') registrationFormElement: NgForm;
  titles$: Observable<Title[]>;
  oneTimePassword:number;
  private subscription = new Subscription();
  userCivilId = '';
  userECCData = '';
  showConfPassword : boolean = false;
  invalidOTPFlag : boolean = false;

  anonymousConsent$: Observable<{
    consent: AnonymousConsent;
    template: string;
  }>;

  registerForm: FormGroup = this.fb.group(
    {
      civilId: ['',Validators.required],
      // eccCustId: [''],
      name: ['', Validators.required],
      // lastName: ['', Validators.required],
      arabicName: [''],
      // arabicLastName: ['', Validators.required],
      uid: ['',Validators.required],
      email: ['', [CustomFormValidators.emailValidator]],
      password: [
        '',
        [Validators.required, CustomFormValidators.passwordValidator],
      ],
      passwordconf: ['', Validators.required],
      // newsletter: new FormControl({
      //   value: false,
      //   disabled: this.isConsentRequired(),
      // }),
      // termsandconditions: [false, Validators.requiredTrue],
      customerType : ['']
    },
    {
      validators: CustomFormValidators.passwordsMustMatch(
        'password',
        'passwordconf'
      ),
    }
  );
  customer: Promise<void>;

  constructor(
    protected auth: AuthService,
    protected authRedirectService: AuthRedirectService,
    protected userService: UserService,
    protected globalMessageService: GlobalMessageService,
    protected fb: FormBuilder,
    protected router: RoutingService,
    protected anonymousConsentsService: AnonymousConsentsService,
    protected anonymousConsentsConfig: AnonymousConsentsConfig,
    protected recaptchaService: RecaptchaService,
    protected registerService: RegisterService,
    private toastr: ToastrService,
    public commonService: CommonService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    $('#civilIdPopup').modal('show');
    
    // this.titles$ = this.userService.getTitles().pipe(
      
    //   tap((titles) => {
    //     if (Object.keys(titles).length === 0) {
    //       this.userService.loadTitles();
    //     }
    //   }),
    //   map((titles) => {
    //     return titles.sort(sortTitles);
    //   })

      
     
    // );
    
   
    this.registerUserProcessInit();

    
    this.subscription.add(
      this.globalMessageService
        .get()
        .pipe(filter((messages) => !!Object.keys(messages).length))
        .subscribe((globalMessageEntities: GlobalMessageEntities) => {
          const messages =
            globalMessageEntities &&
            globalMessageEntities[GlobalMessageType.MSG_TYPE_ERROR];

          if (
            messages &&
            messages.some((message) => message === 'This field is required.')
          ) {
            this.globalMessageService.remove(GlobalMessageType.MSG_TYPE_ERROR);
            this.globalMessageService.add(
              { key: 'register.titleRequired' },
              GlobalMessageType.MSG_TYPE_ERROR
            );
          }
        })


    );

    const { registerConsent } = this.anonymousConsentsConfig?.anonymousConsents;

    this.anonymousConsent$ = combineLatest([
      this.anonymousConsentsService.getConsent(registerConsent),
      this.anonymousConsentsService.getTemplate(registerConsent),
    ]).pipe(
      map(([consent, template]: [AnonymousConsent, ConsentTemplate]) => {
        return {
          consent,
          template: template ? template.description : '',
        };
      })
    );
  }


  
  submitForm(): void {
    if (this.registerForm.valid) {
      this.commonService.submitForm('registerCustomer',this.registerForm).then(status=>{
        const { uid, password } = this.registerForm.controls;
    this.auth.authorize(
      uid.value.toLowerCase(), // backend accepts lowercase emails only
      password.value
    );

    if (!this.sub) {
      this.sub = this.auth.getUserToken().subscribe((data) => {
        if (data && data.access_token) {
          this.globalMessageService.remove(GlobalMessageType.MSG_TYPE_ERROR);
          this.authRedirectService.redirect();
        }
      });
    }
      this.toastr.success('Thank you for registering');
      });
    
      $("#otpPopup").modal('hide');
    } else {
      this.registerForm.markAllAsTouched();
    }    
  }

  registerUser(): void {
    // this.userService.register(
    //   this.collectDataFromRegisterForm(this.registerForm.value)
    // );
    this.registerService.register(
      this.collectDataFromRegisterForm(this.registerForm.value)
    );
  }

  titleSelected(title: Title): void {
    this.registerForm['controls'].titleCode.setValue(title.code);
  }

  collectDataFromRegisterForm(formData: any): UserRegister {
    const { civilId,eccCustId, name, arabicName, mobile, email, password, titleCode } = formData;
    const oneTimePassword=this.oneTimePassword;
    return {
      civilId,
      eccCustId,
      name,
      arabicName,
      oneTimePassword,
      mobile,
      uid : email ? email.toLowerCase() : "",
      password,
      titleCode,
    };
  }
  
  getECCCustomer(){
    var civilId= this.registerForm.controls['civilId'].value;
    if(civilId!=null){
      this.customer=this.registerService.getECCCustomerDetails(civilId).then(pos=>
        {
          var email=`${pos.email}`;
          this.registerForm.get('civilId').patchValue(`${pos.civilId}`);
          this.registerForm.get('eccCustId').patchValue(`${pos.eccCustId}`);
          this.registerForm.get('name').patchValue(`${pos.name}`);
          this.registerForm.get('arabicName').patchValue(`${pos.arabicName}`);
          this.registerForm.get('mobile').patchValue(`${pos.mobile}`);
         
        })   
    }
  }

  sendOTP(){  
    var mobile= this.registerForm.controls['mobile'].value;
    var civilId=this.registerForm.controls['civilId'].value;
    if (mobile!=null) {
      
      this.registerService.sendOtpOnMobile(mobile,civilId);
    }else {
      this.registerForm.markAllAsTouched();
    }
    $("#otpPopup").modal('show');
  }

  submitOtp(){
    this.registrationFormElement.ngSubmit.emit();

  }
  isConsentGiven(consent: AnonymousConsent): boolean {
    return this.anonymousConsentsService.isConsentGiven(consent);
  }

  private isConsentRequired(): boolean {
    const {
      requiredConsents,
      registerConsent,
    } = this.anonymousConsentsConfig?.anonymousConsents;

    if (requiredConsents && registerConsent) {
      return requiredConsents.includes(registerConsent);
    }

    return false;
  }

  private onRegisterUserSuccess(success: boolean): void {
    if (success) {
      this.router.go('/');
      this.globalMessageService.add(
        { key: 'register.postRegisterMessage' },
        GlobalMessageType.MSG_TYPE_CONFIRMATION
      );
    }
  }

  toggleAnonymousConsent(): void {
    const { registerConsent } = this.anonymousConsentsConfig.anonymousConsents;

    if (Boolean(this.registerForm.get('newsletter').value)) {
      this.anonymousConsentsService.giveConsent(registerConsent);
    } else {
      this.anonymousConsentsService.withdrawConsent(registerConsent);
    }
  }

  private registerUserProcessInit(): void {
    this.userService.resetRegisterUserProcessState();
    this.subscription.add(
      this.userService.getRegisterUserResultSuccess().subscribe((success) => {
        this.onRegisterUserSuccess(success);
      })
    );
  }

  submitCivilId() {
    this.commonService.postRequest('sendOTP',null,this.userCivilId).then(
      resp => {
        this.registerForm.get('civilId').patchValue(this.userCivilId);
        if(resp.data){
          $('#userOTPModal').modal('show');
          $('#civilIdPopup').modal('hide');
        }else{
          $('#civilIdPopup').modal('hide');
        }
      }
    );
    
  }

  submitUserOTP() {
    this.invalidOTPFlag = false;
    this.commonService.postRequest('validateOTP',null,this.userCivilId,this.oneTimePassword).then(
      resp => {
        if(null != resp.data){
          $('#userOTPModal').modal('hide');
          this.populateUserEccData(resp.data);
        }else{
          
          this.invalidOTPFlag = true;
          this.cd.detectChanges();
        }
      }
    );
  }

  populateUserEccData(userEccData){
    if(!isEmpty(userEccData)){
      setFormField(this.registerForm,"name",userEccData.name);
      setFormField(this.registerForm,"arabicName",userEccData.arabicName);
      setFormField(this.registerForm,"uid",userEccData.mobile);
      setFormField(this.registerForm,"email",userEccData.email);
      setFormField(this.registerForm,"customerType",userEccData.customerType);
    }else{
      setFormField(this.registerForm,"customerType","GUEST");
    }
    
  }

  onOtpChange(event) {
    this.oneTimePassword = event;
  }

  toggleShowConfPassword() {
    this.showConfPassword = !this.showConfPassword;
  }

  ngOnDestroy() {
    $('#civilIdPopup').modal('hide');
    $('#userOTPModal').modal('hide');
    this.subscription.unsubscribe();
    this.userService.resetRegisterUserProcessState();
  }

  resendOTP(){
    this.submitCivilId();
    this.toastr.success("OTP resent.")
  }
  
}
