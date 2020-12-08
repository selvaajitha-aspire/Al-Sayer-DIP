import { ServiceHistoryService } from './../../services/service-history/service-history.service';
import { Component, OnInit } from '@angular/core';
import { RoadsideAssistanceService } from '../../services/roadside-assistance/roadside-assistance.service';


@Component({
  selector: 'app-service-history',
  templateUrl: './service-history.component.html',
  styleUrls: ['./service-history.component.scss']
})
export class ServiceHistoryComponent implements OnInit {
  serviceHistoryList;
  vehicleList;
  serviceHistoryToggle = {};

  constructor( protected service:ServiceHistoryService,
    private assistanceService: RoadsideAssistanceService ) { }

  ngOnInit(): void {
    this.vehicleList=this.assistanceService.getVehicles() || [];
  }

  getChasisNumber(chassisNumber) {
    this.serviceHistoryList=this.service.getServiceHistory(chassisNumber) || [];
  }

}

