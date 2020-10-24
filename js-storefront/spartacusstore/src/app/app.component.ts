import { Component, OnInit } from '@angular/core';
import { AuthService, User, UserService } from '@spartacus/core';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'spartacusstore';
  user$: Observable<User>;
  constructor(
    private auth: AuthService, private userService: UserService
  ) { }
  ngOnInit() {
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
