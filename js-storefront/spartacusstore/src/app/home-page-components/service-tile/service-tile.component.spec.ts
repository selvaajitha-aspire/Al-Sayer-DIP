import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceTileComponent } from './service-tile.component';

describe('CustomTileComponent', () => {
  let component: ServiceTileComponent;
  let fixture: ComponentFixture<ServiceTileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ServiceTileComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServiceTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
