import { Component, OnInit } from '@angular/core';
import { AuthService, GlobalMessageService, GlobalMessageType, RoutingService, User, UserService } from '@spartacus/core';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  user$: Observable<User>;
  isLoggedIn: boolean;
  constructor(
    private auth: AuthService, private userService: UserService, protected router: RoutingService, private authService: AuthService,
    private globalMessageService: GlobalMessageService
  ) { }
  ngOnInit() {
    this.authService.isUserLoggedIn().subscribe((data) =>{
      this.isLoggedIn = data;
      if(!this.isLoggedIn){
        this.router.go("/login");
      }else{
        this.globalMessageService.remove(GlobalMessageType.MSG_TYPE_ERROR);
      }
    })
    this.user$ = this.auth.isUserLoggedIn().pipe(
      switchMap((isUserLoggedIn) => {
        if (isUserLoggedIn) {
          return this.userService.get();
        } else {
          return of(undefined);
        }
      })
    );
  }
}
