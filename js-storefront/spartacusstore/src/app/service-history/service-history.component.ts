import { ServiceHistoryService } from './../../services/service-history/service-history.service';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-service-history',
  templateUrl: './service-history.component.html',
  styleUrls: ['./service-history.component.scss']
})
export class ServiceHistoryComponent implements OnInit {
  serviceHistoryList;
  vehicleToggle = {};

  constructor( protected service:ServiceHistoryService ) { }

  ngOnInit(): void {
    this.serviceHistoryList=this.service.getServiceHistory() || [];
  }

}

