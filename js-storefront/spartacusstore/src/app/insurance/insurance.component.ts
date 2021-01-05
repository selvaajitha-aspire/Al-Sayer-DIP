
import { Component, OnInit, HostListener } from '@angular/core';
import { InsuranceService } from 'src/services/insurance.service';

@Component({
  selector: 'app-insurance',
  templateUrl: './insurance.component.html',
  styleUrls: ['./insurance.component.scss']
})
export class InsuranceComponent implements OnInit {
  insurances;
  insuranceToggle = {};
  innerHeight: any = 'auto';
  

  constructor( protected service:InsuranceService ) { }

  ngOnInit(): void {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
    this.insurances=this.service.getInsuranceList() || [];
  }

 
 

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.innerHeight = window && window.innerHeight;
    this.innerHeight = this.innerHeight ? this.innerHeight-120 + 'px': 'auto';
  }
}
