import { TestBed } from '@angular/core/testing';

import { ServiceHistoryService } from './service-history.service';

describe('ServiceHistoryService', () => {
  let service: ServiceHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
