import { TestBed } from '@angular/core/testing';

import { HeaderTitleService } from './header-title.service';

describe('ChangeHeaderTitleService', () => {
  let service: HeaderTitleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeaderTitleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
