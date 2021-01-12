import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UseMyLocationComponent } from './use-my-location.component';

describe('UseMyLocationComponent', () => {
  let component: UseMyLocationComponent;
  let fixture: ComponentFixture<UseMyLocationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UseMyLocationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UseMyLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
