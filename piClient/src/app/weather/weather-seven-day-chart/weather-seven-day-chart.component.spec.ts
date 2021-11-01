import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeatherSevenDayChartComponent } from './weather-seven-day-chart.component';

describe('WeatherSevenDayChartComponent', () => {
  let component: WeatherSevenDayChartComponent;
  let fixture: ComponentFixture<WeatherSevenDayChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WeatherSevenDayChartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WeatherSevenDayChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
