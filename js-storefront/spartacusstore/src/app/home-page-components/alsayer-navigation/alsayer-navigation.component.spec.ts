import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlsayerNavigationComponent } from './alsayer-navigation.component';

describe('AlsayerNavigationComponent', () => {
  let component: AlsayerNavigationComponent;
  let fixture: ComponentFixture<AlsayerNavigationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlsayerNavigationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlsayerNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
