import { Component, OnInit } from '@angular/core';
import * as $ from "jquery";
import { DeviceDetectorService } from 'ngx-device-detector';
@Component({
  selector: 'app-signin-menu',
  templateUrl: './signin-menu.component.html',
  styleUrls: ['./signin-menu.component.scss']
})
export class SigninMenuComponent implements OnInit{
  isMobile: boolean;
  isDropdownOpen : boolean;
  constructor(private deviceService: DeviceDetectorService) {
    this.isMobile = false;
    this.epicFunction();
  }
  epicFunction() {
    this.isMobile = this.deviceService.isMobile();
  }

  ngOnInit(){
    this.isDropdownOpen = false;
  }

  openMenu() {
    if (this.isMobile) {
      if (!this.isDropdownOpen) {
        $('.header').css("height", "450px");
        $('.bar-icon-mobile').css("margin-top", "400px");
        $('#myNav').height("400px");
        $('body').css("overflow", "hidden");
        this.isDropdownOpen = true;
      } else {
        $('#myNav').height("0%");
        $('.header').css("height", "30px");
        $('.bar-icon-mobile').css("margin-top", "25px");
        $('body').css("overflow", "auto");
        this.isDropdownOpen = false;
      }
    } else {
      $('#myNav').css("height", "100%");
    }
  }
  closeMenu() {
    $('#myNav').css("height", "0");
  }
}
