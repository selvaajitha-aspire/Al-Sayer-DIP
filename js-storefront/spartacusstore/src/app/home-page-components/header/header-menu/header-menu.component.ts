import { Component, OnInit, AfterViewInit, Inject, ViewChild, ElementRef, HostListener, ChangeDetectorRef } from '@angular/core';
import { trigger, state, style, transition, animate} from '@angular/animations';
import { CmsNavigationComponent, CmsService } from '@spartacus/core';
import { CmsComponentData, NavigationNode  } from '@spartacus/storefront';
import { Observable } from 'rxjs';
import { DeviceDetectorService } from 'ngx-device-detector';
import { Router, NavigationEnd, Event } from '@angular/router';
import { AuthService, User, UserService } from '@spartacus/core';
import { AlsayerNavigationService } from '../alsayer-navigation.service';
import { DOCUMENT } from '@angular/common';
import { HeaderTitleService } from '../../../../services/header-title/header-title.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { CommonService } from 'src/services/common/common.services';
import { environment } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';

declare var $: any;

@Component({
  selector: 'app-header-menu',
  templateUrl: './header-menu.component.html',
  styleUrls: ['./header-menu.component.scss'],
  animations: [
    trigger('slideInOut', [
      state('in', style({
        transform: 'translate3d(0, 0, 0)'
      })),
      state('out', style({
        transform: 'translate3d(-100%, 0, 0)'
      })),
      transition('in => out', animate('400ms ease-in-out')),
      transition('out => in', animate('400ms ease-in-out'))
    ]),
  ]
})
export class HeaderMenuComponent implements OnInit, AfterViewInit {
  @ViewChild('stickyMenu') menuElement: ElementRef;
  menuPosition = 50;
  @HostListener('window:scroll', ['$event'])
    handleScroll(){
        const windowScroll = window.pageYOffset;
        let el = this.menuElement.nativeElement;
        if(windowScroll > this.menuPosition){
            el.style.background = 'rgba(255,255,255,0.5)';
        } else {
          el.style.background = 'rgba(0,0,0,0)';
        }
    }

  node$: Observable<NavigationNode> = this.service.createNavigation(
    this.componentData.data$
  );
  profilePhotoChanged = false;
  menuState:string = 'out';
  isLoggedIn = false;
  user$: Observable<any>;
  navigationLinks: NavigationNode[];
  hideHeaderForRouting: any[] = ["/login", "/login/forgot-password", "/login/register"];
  title: String = '';
  fileName: String;
  profilePhotoForm: FormGroup = this.fb.group(
    {
      profilePhoto: ['']
    }
  );
  profilePhotoUrl : any = "../../../../assets/icons/alsayerlogo.png"; 
  previousProfilePhotoUrl : any;

  constructor(
    public commonService:CommonService,protected fb: FormBuilder, 
    public componentData: CmsComponentData<CmsNavigationComponent>,
    protected service: AlsayerNavigationService,
    private userService: UserService,
    private router: Router,
    @Inject(DOCUMENT) private document: Document,
    private headerTitle: HeaderTitleService,
    cmsService: CmsService,
    private cd: ChangeDetectorRef,
    private toastr: ToastrService,
  ) {
    this.user$ = this.userService.get();
    this.user$.subscribe(user => {
      if (Object.keys(user).length === 0 && user.constructor === Object) {
        this.isLoggedIn = false;
      } else {
        this.isLoggedIn = true;
      }
    });
    // this.headerTitle.headerTitle.subscribe((data: String) => {
    //   this.title = data;
    // });
    cmsService.getCurrentPage().subscribe(data => {
      this.title = data ? data.title: '';
    })
  }
  
  ngAfterViewInit(){
    this.menuPosition = this.menuElement.nativeElement.offsetTop
  }
  ngOnInit(){
    this.router.events.subscribe((event: Event) => {
      if(event instanceof NavigationEnd){
        // this.title = '';
        this.hideHeader(event.url);
      }
    });
    this.hideHeader(this.router.url);
    this.user$.subscribe(resp=>{
      this.profilePhotoUrl = this.getUrl(resp.profilePhotoUrl);
    });
  }

 
  toggleMenu() {
    this.menuState = this.menuState === 'out' ? 'in' : 'out';
  }
  hideHeader(url) {
    let headerFlag = true;
    this.hideHeaderForRouting.map(routerLink => {
    routerLink === url ? headerFlag = false: "";
    });
    let display = headerFlag ? 'block' : 'none';
    let headerElements = this.document.getElementsByTagName('header') &&
    Array.from(this.document.getElementsByTagName('header') as HTMLCollectionOf<HTMLElement>);
    let footerElements = this.document.getElementsByTagName('footer') &&
    Array.from(this.document.getElementsByTagName('footer') as HTMLCollectionOf<HTMLElement>);
    headerElements.map(element => {
    element.style.display = display;
    });
    footerElements.map(element => {
      element.style.display = display;
    });
    }

    onProfileSelect(event) {
      if (event.target.files.length > 0) {
        this.fileName = event.target.files[0]['name'];
        const attachments = event.target.files[0];
        console.log(attachments);
        this.profilePhotoForm.patchValue({
          profilePhoto: attachments
        });
        this.profilePhotoForm.get('profilePhoto').updateValueAndValidity();
        this.previousProfilePhotoUrl = this.profilePhotoUrl;
        var reader = new FileReader();
          reader.readAsDataURL(attachments);
          reader.onload = (_event) => {
          this.profilePhotoUrl = reader.result;
          this.profilePhotoChanged = true;
          this.cd.detectChanges();
        }

        
      }
    }

    uploadPhoto(event): void {
      event.preventDefault();
      this.commonService.submitAttachment('updateProfilePhoto',this.profilePhotoForm,'profilePhoto').then(resp=>{
        if(resp){
          console.log("success");
          this.toastr.success('Your Profile Photo Has been Changed', 'Thank you!');
        }else{
          console.log("failure");
          this.toastr.error("Error while updating profile picture", 'Please try after some time!');
          this.profilePhotoUrl = this.previousProfilePhotoUrl;
        }
        this.profilePhotoChanged = false;
        this.cd.detectChanges();
      });
    }

      cancelUploadPhoto(event): void {
        event.preventDefault();
        this.profilePhotoUrl = this.previousProfilePhotoUrl;
        this.profilePhotoChanged = false;
    }

    openProfilePhotoModal(){
      $('#updateProfilePhotoModal').modal('show');
    }

    getUrl(url: string) {
      if (url && !url.startsWith("/")) {
        url = "/" + url;
      }
      return environment.occBaseUrl + url;
    }
}
