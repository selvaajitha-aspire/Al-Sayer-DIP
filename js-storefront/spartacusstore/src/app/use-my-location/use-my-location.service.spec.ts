import { TestBed } from '@angular/core/testing';

import { UseMyLocationService } from './use-my-location.service';

describe('UseMyLocationService', () => {
  let service: UseMyLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UseMyLocationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
