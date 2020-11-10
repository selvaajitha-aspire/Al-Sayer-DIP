import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyTicketsComponent } from './my-tickets.component';
import { CmsConfig, ConfigModule } from '@spartacus/core';
import { ticketsOccConfig } from '../config/tickets-config';




@NgModule({
  declarations: [MyTicketsComponent],
  imports: [
    CommonModule,
    ConfigModule.withConfig(ticketsOccConfig),
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        MyTicketsComponent: {
          component:MyTicketsComponent ,
        },
      },
    }),
  ]
})
export class MyTicketsModule { }
