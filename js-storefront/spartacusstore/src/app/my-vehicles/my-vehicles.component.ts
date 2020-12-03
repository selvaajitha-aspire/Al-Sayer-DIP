import { RoadsideAssistanceService } from './../../services/roadside-assistance/roadside-assistance.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-vehicles',
  templateUrl: './my-vehicles.component.html',
  styleUrls: ['./my-vehicles.component.scss']
})
export class MyVehiclesComponent implements OnInit {

  vehicleList;
  vehicleToggle = {};
  constructor(protected service:RoadsideAssistanceService) {}

  ngOnInit(): void {
    this.vehicleList=this.service.getVehicles() || [];
    this.vehicleList = [{
      modline: 'Prod 150-v6',
      warrantyType: 'MNSS-14 Extended Warrenty',
      warrantyExpiryDate: '20-12-2020'
    },{
      modline: 'Prod 150-v6',
      warrantyType: 'MNSS-14 Extended Warrenty',
      warrantyExpiryDate: '20-12-2020'
    },
    {
      modline: 'Prod 150-v6',
      warrantyType: 'MNSS-14 Extended Warrenty',
      warrantyExpiryDate: '20-12-2020'
    },
    {
      modline: 'Prod 150-v6',
      warrantyType: 'MNSS-14 Extended Warrenty',
      warrantyExpiryDate: '20-12-2020'
    }
  ]
  }

  toggleVehicle(){
    
  }

}
