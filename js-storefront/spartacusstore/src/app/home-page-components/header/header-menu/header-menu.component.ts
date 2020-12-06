import { Component, OnInit, AfterViewInit, Inject } from '@angular/core';
import { trigger, state, style, transition, animate} from '@angular/animations';
import { CmsNavigationComponent } from '@spartacus/core';
import { CmsComponentData, NavigationNode  } from '@spartacus/storefront';
import { Observable } from 'rxjs';
import { DeviceDetectorService } from 'ngx-device-detector';
import { Router, NavigationEnd, Event } from '@angular/router';
import { AuthService, User, UserService } from '@spartacus/core';
import { AlsayerNavigationService } from '../alsayer-navigation.service';
import { DOCUMENT } from '@angular/common';
import { HeaderTitleService } from '../../../../services/header-title/header-title.service';

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
export class HeaderMenuComponent implements OnInit {
  node$: Observable<NavigationNode> = this.service.createNavigation(
    this.componentData.data$
  );

  menuState:string = 'out';
  isLoggedIn = false;
  user$: Observable<User>;
  navigationLinks: NavigationNode[];
  hideHeaderForRouting: any[] = ["/login", "/login/forgot-password", "/login/register"];
  title: String = '';
  constructor(
    public componentData: CmsComponentData<CmsNavigationComponent>,
    protected service: AlsayerNavigationService,
    private userService: UserService,
    private router: Router,
    @Inject(DOCUMENT) private document: Document,
    private headerTitle: HeaderTitleService
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
    this.headerTitle.headerTitle.subscribe((data: String) => {
      this.title = data;
    });
  }
  ngOnInit(){
    this.router.events.subscribe((event: Event) => {
      if(event instanceof NavigationEnd){
        this.title = '';
      
      }
    });
  }

 
  toggleMenu() {
    this.menuState = this.menuState === 'out' ? 'in' : 'out';
  }
}
