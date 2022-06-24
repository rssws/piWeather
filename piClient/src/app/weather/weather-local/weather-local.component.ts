import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { WeatherResponse } from '../../model/weather/weather-response';
import { WeatherService } from '../weather.service';

@Component({
  selector: 'app-weather-local',
  templateUrl: './weather-local.component.html',
  styleUrls: ['./weather-local.component.css'],
})
export class WeatherLocalComponent implements OnInit, OnChanges {
  @Input() localWeatherResponse: WeatherResponse;
  @Input() name: string;
  @Output() notifyLocalWeatherResponse: EventEmitter<WeatherResponse> = new EventEmitter<WeatherResponse>();

  weatherResponseLoading = true;
  cityNameFontSize: string;
  errorMessage: string;

  constructor(private weatherService: WeatherService) {}

  ngOnInit(): void {}

  ngOnChanges(): void {
    this.weatherResponseLoading = true;
    if (this.localWeatherResponse !== undefined) {
      this.weatherResponseLoading = false;
    } else {
      this.updateLocalWeather();
    }
  }

  updateLocalWeather(): void {
    const weatherResponse$ = this.weatherService.getLocalWeatherResponseByName(this.name);
    weatherResponse$.subscribe(
      (r) => {
        if (r.responseStatus.toString() !== 'SUCCESS') {
          this.weatherResponseLoading = true;
          this.errorMessage = r.responseMessage;
          return;
        }
        this.localWeatherResponse = r;
        this.notifyLocalWeatherResponse.emit(this.localWeatherResponse);

        this.weatherResponseLoading = false;
        this.errorMessage = undefined;
      },
      (error) => {
        this.weatherResponseLoading = true;
        this.errorMessage = error.message;
        setTimeout(this.updateLocalWeather.bind(this), 6000);
      }
    );
  }
}
