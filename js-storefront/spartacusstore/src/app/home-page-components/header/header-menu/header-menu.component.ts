import { Component, OnInit, AfterViewInit } from '@angular/core';
import { trigger, state, style, transition, animate} from '@angular/animations';
import { CmsNavigationComponent } from '@spartacus/core';
import { CmsComponentData, NavigationNode, NavigationService } from '@spartacus/storefront';
import * as $ from "jquery";
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DeviceDetectorService } from 'ngx-device-detector';
import { Router, NavigationEnd, NavigationStart } from '@angular/router';
import { AuthService, User, UserService } from '@spartacus/core';
import { switchMap } from 'rxjs/operators';

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
  node$: Observable<NavigationNode> = this.service.createNavigation(
    this.componentData.data$
  );

  menuState:string = 'out';

  // styleClass$: Observable<string> = this.componentData.data$.pipe(
  //   map((d) => d?.styleClass)
  // );
  // isMobile: boolean;
  // isDropdownOpen: boolean;
  // forceClose: boolean;
  isLoggedIn = false;
  user$: Observable<User>;
  navigationLinks: NavigationNode[];
  constructor(
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected service: NavigationService,
    private deviceService: DeviceDetectorService,
    private router: Router,
    private auth: AuthService,
    private userService: UserService
  ) {
    this.user$ = this.userService.get();
    this.user$.subscribe(user => {
      console.log('userId', user);
      if (Object.keys(user).length === 0 && user.constructor === Object) {
        this.isLoggedIn = false;
      } else {
        this.isLoggedIn = true;
      }
      console.log('isLoggedIn',this.isLoggedIn)
    });

  //   router.events.subscribe((val) => {
  //     if (val instanceof NavigationStart) {
  //       this.isDropdownOpen = false;
  //       this.forceClose = true;
  //       this.openMenu();
  //     }
  // });
    // this.isMobile = false;
    // this.epicFunction();
  }
  ngOnInit(){
    // this.isDropdownOpen = false;
    // $(".SiteLogo a").unbind("click").click((event) => {
    //   if (this.forceClose) {
    //     this.isDropdownOpen = false;
    //   } else {
    //     this.isDropdownOpen = !this.isDropdownOpen;
    //   }
    //   this.forceClose = false;
    //   this.openMenu();
    // });
    this.node$.subscribe(data=> {
      if(data && data.title == 'My Account') {
        this.navigationLinks = data.children?.length > 0 ?  data.children[0].children : [];
      }
    })
  }

  ngAfterViewInit() {
    document.getElementsByClassName('header-wrapper ')[1] ? document.getElementsByClassName('header-wrapper ')[1]['style'].display = 'none' : '';
  }

  // ngAfterContentChecked() {
    // if (event && event.target["tagName"] == "NAV") {
    //   if(this.isMobile){
    //     $('#myNav1').css("height", "0");
    //     $('body').css("overflow", "auto");
    //     $('.header').css("height", "50px");
    //     $('#ar-mobile').removeClass("bar-icon-mobile-menu-open");
    //     $('#bar-mobile').addClass("bar-icon-mobile-menu-close");
    //     this.isDropdownOpen = false;
    //   } 
    // }
  // }

  openMenu() {
    // if (this.isMobile) {
    //     if (this.isDropdownOpen) {
    //       if(this.isLoggedIn){
    //         $('header').height("600px");
    //         $('.SiteLogo').css("marginTop","560px");
    //       } else {
    //         $('header').height("300px");
    //         $('.SiteLogo').css("marginTop","260px");
    //       }
    //     $('header').css("transition","0.3s"); 
    //     $('.header').css("height", "80vh");        
    //     $('#bar-mobile').removeClass("bar-icon-mobile-menu-close");
    //     $('#bar-mobile').addClass("bar-icon-mobile-menu-open");
    //     $('#myNav1').height("80vh"); 
    //     $('body').css("overflow", "hidden");
    //     this.isDropdownOpen = true;
    //   } else {
    //     $('#myNav1').height("0%");
    //     $('header').height("40px"); 
    //     $('header').css("transition","0.5s"); 
    //     $('.header').css("height", "40px");        
    //     $('.SiteLogo').css("marginTop","0");
    //     $('.SiteLogo').css("transition","0.3s");
    //     $('#ar-mobile').removeClass("bar-icon-mobile-menu-open");
    //     $('#bar-mobile').addClass("bar-icon-mobile-menu-close");
    //     $('body').css("overflow", "auto");
    //     this.isDropdownOpen = false;
    //   }
    // } else {
    //   $('#myNav1').css("height", "100vh"); 
    // }
  }
  closeMenu() {
    // $('#myNav1').css("height", "0");
  }

  toggleMenu() {
    this.menuState = this.menuState === 'out' ? 'in' : 'out';
  }
}
