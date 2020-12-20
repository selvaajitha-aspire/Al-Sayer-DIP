import { ServiceHistoryService } from './../../services/service-history/service-history.service';
import { Component, OnInit, HostListener } from '@angular/core';
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
  innerHeight: any = 'auto';
  selectedChassisNumber = '';

  constructor( protected service:ServiceHistoryService,
    private assistanceService: RoadsideAssistanceService ) { }

  ngOnInit(): void {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
    this.vehicleList=this.assistanceService.getVehicles() || [];
  }

  getChasisNumber(chassisNumber) {
    this.selectedChassisNumber = chassisNumber;
    this.serviceHistoryList=this.service.getServiceHistory(chassisNumber) || [];
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
  }

}

