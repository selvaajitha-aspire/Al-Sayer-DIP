import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoadsideAssistanceComponent } from './roadside-assistance.component';

describe('RoadsideAssistanceComponent', () => {
  let component: RoadsideAssistanceComponent;
  let fixture: ComponentFixture<RoadsideAssistanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoadsideAssistanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoadsideAssistanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
