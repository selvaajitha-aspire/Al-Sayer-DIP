import { TestBed } from '@angular/core/testing';

import { RoadsideAssistanceService } from './roadside-assistance.service';

describe('RoadsideAssistanceService', () => {
  let service: RoadsideAssistanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoadsideAssistanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
