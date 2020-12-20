import { RoadsideAssistanceService } from './../../services/roadside-assistance/roadside-assistance.service';
import { Component, OnInit, HostListener } from '@angular/core';
import { HeaderTitleService } from './../../services/header-title/header-title.service';

@Component({
  selector: 'app-my-vehicles',
  templateUrl: './my-vehicles.component.html',
  styleUrls: ['./my-vehicles.component.scss']
})
export class MyVehiclesComponent implements OnInit {

  vehicleList;
  vehicleToggle = {};
  innerHeight: any = 'auto';
  constructor(protected service:RoadsideAssistanceService,
    private headerTitle : HeaderTitleService
    ) {}

  ngOnInit(): void {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
    this.vehicleList=this.service.getVehicles() || [];
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
  }

}
