import { Component, OnInit } from '@angular/core';
import { DeviceDetectorService } from 'ngx-device-detector';
@Component({
  selector: 'app-signin-menu',
  templateUrl: './signin-menu.component.html',
  styleUrls: ['./signin-menu.component.scss']
})
export class SigninMenuComponent implements OnInit{
  // isMobile: boolean;
  constructor(private deviceService: DeviceDetectorService) {
    // this.isMobile = false;
  }
  epicFunction() {
    // this.isMobile = this.deviceService.isMobile();
  }

  ngOnInit(){
  }
}
