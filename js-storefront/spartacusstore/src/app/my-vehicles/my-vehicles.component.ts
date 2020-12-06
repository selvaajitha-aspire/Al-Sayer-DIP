import { RoadsideAssistanceService } from './../../services/roadside-assistance/roadside-assistance.service';
import { Component, OnInit } from '@angular/core';
import { HeaderTitleService } from './../../services/header-title/header-title.service';

@Component({
  selector: 'app-my-vehicles',
  templateUrl: './my-vehicles.component.html',
  styleUrls: ['./my-vehicles.component.scss']
})
export class MyVehiclesComponent implements OnInit {

  vehicleList;
  vehicleToggle = {};
  constructor(protected service:RoadsideAssistanceService,
    private headerTitle : HeaderTitleService
    ) {}

  ngOnInit(): void {
    this.headerTitle.headerTitle.next('my vehicles')
    this.vehicleList=this.service.getVehicles() || [];
  }

}
