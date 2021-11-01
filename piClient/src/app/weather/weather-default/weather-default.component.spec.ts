import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeatherDefaultComponent } from './weather-default.component';

describe('WeatherDefaultComponent', () => {
  let component: WeatherDefaultComponent;
  let fixture: ComponentFixture<WeatherDefaultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WeatherDefaultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WeatherDefaultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
