import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeatherHourlyChartComponent } from './weather-hourly-chart.component';

describe('WeatherHourlyChartComponent', () => {
  let component: WeatherHourlyChartComponent;
  let fixture: ComponentFixture<WeatherHourlyChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WeatherHourlyChartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WeatherHourlyChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
