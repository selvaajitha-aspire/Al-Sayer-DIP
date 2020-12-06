import { ToastrService } from 'ngx-toastr';
import { Component, OnInit, ViewChild, ChangeDetectionStrategy, NgZone } from '@angular/core';
import {} from 'googlemaps';
import {
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { RoutingService } from '@spartacus/core';
import { CommonService } from "../../services/common/common.services"; 

export class CommonComponent {

  public commonService;

constructor(){
  this.commonService = CommonService
}

}