import { Component, OnInit } from '@angular/core';
import { UserAddressService, RoutingService } from '@spartacus/core';
import { CheckoutConfigService } from '@spartacus/storefront';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-service-type',
  templateUrl: './service-type.component.html',
  styleUrls: ['./service-type.component.scss']
})
export class ServiceTypeComponent implements OnInit {
  
 

  constructor(protected userAddressService: UserAddressService,
    protected routingService: RoutingService,
    protected checkoutConfigService: CheckoutConfigService,
    protected activatedRoute: ActivatedRoute,) { }

  ngOnInit(): void {
  }

  goNext(): void {
    this.routingService.go(
      this.checkoutConfigService.getNextCheckoutStepUrl(this.activatedRoute)
    );
  }

  goPrevious(): void {
    this.routingService.go(
      this.checkoutConfigService.getPreviousCheckoutStepUrl(
        this.activatedRoute
      ) || 'cart'
    );
  }

}
