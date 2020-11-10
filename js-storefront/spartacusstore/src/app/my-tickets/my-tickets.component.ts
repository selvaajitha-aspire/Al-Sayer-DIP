import { Component, OnInit } from '@angular/core';
import { MyTicketsService } from 'src/services/my-tickets/my-tickets.service';

@Component({
  selector: 'app-my-tickets',
  templateUrl: './my-tickets.component.html',
  styleUrls: ['./my-tickets.component.scss']
})
export class MyTicketsComponent implements OnInit {

  ticketsList;
  constructor(protected service:MyTicketsService) {}

  ngOnInit(): void {
    this.ticketsList=this.service.getRsaRequests() || [];
   
  }

}
