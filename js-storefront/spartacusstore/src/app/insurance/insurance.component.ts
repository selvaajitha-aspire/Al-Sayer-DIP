import { Component, OnInit, HostListener } from '@angular/core';
import { InsuranceService } from 'src/services/insurance.service';
import { RoadsideAssistanceService } from 'src/services/roadside-assistance/roadside-assistance.service';

@Component({
  selector: 'app-insurance',
  templateUrl: './insurance.component.html',
  styleUrls: ['./insurance.component.scss']
})
export class InsuranceComponent implements OnInit {
  insurances;
  vehicleList;
  insuranceToggle = {};
  innerHeight: any = 'auto';
  selectedChassisNumber = '';

  constructor( protected service:InsuranceService,
    private assistanceService: RoadsideAssistanceService ) { }

  ngOnInit(): void {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
    this.vehicleList=this.assistanceService.getVehicles() || [];
  }

  getChasisNumber(chassisNumber) {
    this.selectedChassisNumber = chassisNumber;
    this.insurances=this.service.getInsuranceList(chassisNumber) || [];
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
  }
}
