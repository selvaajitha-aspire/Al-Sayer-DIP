import { RoadsideAssistanceService } from './../../services/roadside-assistance/roadside-assistance.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-vehicles',
  templateUrl: './my-vehicles.component.html',
  styleUrls: ['./my-vehicles.component.scss']
})
export class MyVehiclesComponent implements OnInit {

  vehicleList;
  constructor(protected service:RoadsideAssistanceService) {}

  ngOnInit(): void {
    this.vehicleList=this.service.getVehicles() || [];
   
  }

}
