import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeatherLocalComponent } from './weather-local.component';

describe('WeatherLocalComponent', () => {
  let component: WeatherLocalComponent;
  let fixture: ComponentFixture<WeatherLocalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WeatherLocalComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WeatherLocalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
