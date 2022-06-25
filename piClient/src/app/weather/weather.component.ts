import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Coord } from '../model/weather/coord';
import { WeatherService } from './weather.service';
import { WeatherResponse } from '../model/weather/weather-response';
import { DailyWeatherResponse } from '../model/weather/daily-weather-response';
import { HourlyWeatherResponse } from '../model/weather/hourly-weather-response';
import { CookieService } from 'ngx-cookie-service';
import { Settings } from '../model/settings';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css'],
})
export class WeatherComponent implements OnInit {
  settings: Settings;

  city = undefined;
  cityShort = undefined;
  coord = undefined;
  localName = 'home';

  weatherResponse: WeatherResponse;
  dailyWeatherResponse: DailyWeatherResponse;
  hourlyWeatherResponse: HourlyWeatherResponse;
  localWeatherResponse: WeatherResponse;

  errorMessage: string;
  locationLoading = true;

  currentPage = 0;
  currentTimer = 1000;

  constructor(
    private weatherService: WeatherService,
    private route: ActivatedRoute,
    private cookieService: CookieService
  ) {}

  ngOnInit(): void {
    const settingsString = this.cookieService.get('settings');
    if (!settingsString) {
      this.settings = {
        display: {
          weatherDefault: {
            show: true,
            index: 0,
          },
          weatherDaily: {
            show: true,
            index: 1,
          },
          weatherHourly: {
            show: true,
            index: 2,
          },
          weatherLocal: {
            show: false,
            index: 3,
          },
        },
      } as Settings;
      this.cookieService.set('settings', JSON.stringify(this.settings));
      window.location.reload();
    } else {
      this.settings = JSON.parse(settingsString);
    }

    // It enables customized width. If a specify display cannot show full width, this may solve the problem.
    this.route.queryParams.subscribe((params) => {
      if (!isNaN(parseInt(params.w, 10))) {
        document.body.style.width = params.w + 'px';
      }
    });

    this.initWeather();
    this.initLocalWeather();

    setInterval(() => {
      if (this.currentTimer === 1000) {
        this.currentTimer = 0;
        if (!this.locationLoading) {
          this.currentPage = (this.currentPage + 1) % Object.keys(this.settings.display).length;
          console.log(this.currentPage);
        }
        const key: string = Object.keys(this.settings.display).filter((key) => {
          console.log(key);
          console.log(this.settings.display[key]);
          return this.settings.display[key].index === this.currentPage;
        })[0];
        console.log(key);

        if (!this.settings.display[key].show) {
          // skip this page if show === false
          this.currentTimer = 999;
        }
      }
      this.currentTimer += 1;
    }, 10);
  }

  initWeather(): void {
    const jsonResponse$ = this.weatherService.getLocationByIP();
    jsonResponse$.subscribe(
      (response) => {
        const data = JSON.stringify(response);
        const jsonResponse = JSON.parse(data);
        if (!jsonResponse?.ipGeolocation) {
          this.city = 'Berlin, DE';
          this.cityShort = 'Berlin';
        } else {
          this.city = jsonResponse.ipGeolocation.city + ', ' + jsonResponse.ipGeolocation.countryCode;
          this.cityShort = jsonResponse.ipGeolocation.city;
        }

        setInterval(() => {
          this.weatherResponse = undefined;
        }, 1000 * 60 * 30);
        setInterval(() => {
          this.dailyWeatherResponse = undefined;
        }, 1000 * 60 * 60);
        setInterval(() => {
          this.hourlyWeatherResponse = undefined;
        }, 1000 * 60 * 30);
        this.locationLoading = false;
        this.errorMessage = undefined;
      },
      (error) => {
        this.errorMessage = '[Error Message]: ' + error.message;
        setTimeout(this.initWeather.bind(this), 10000);
      }
    );
  }

  initLocalWeather() {
    const jsonResponse$ = this.weatherService.getLocalWeatherResponseByName(this.localName);
    jsonResponse$.subscribe(
      (response) => {
        const data = JSON.stringify(response);
        const jsonResponse = JSON.parse(data);
        if (jsonResponse) {
          this.localWeatherResponse = jsonResponse;
        }
        setInterval(() => {
          this.localWeatherResponse = undefined;
        }, 1000 * 15);
      },
      (error) => {
        this.errorMessage = '[Error Message]: ' + error.message;
        setTimeout(this.initWeather.bind(this), 10000);
      }
    );
  }

  setCoord(coord: Coord): void {
    this.coord = coord;
  }

  setWeatherResponse(weatherResponse: WeatherResponse): void {
    this.weatherResponse = weatherResponse;
  }
  setDailyWeatherResponse(dailyWeatherResponse: DailyWeatherResponse): void {
    this.dailyWeatherResponse = dailyWeatherResponse;
  }
  setHourlyWeatherResponse(hourlyWeatherResponse: HourlyWeatherResponse): void {
    this.hourlyWeatherResponse = hourlyWeatherResponse;
  }
  setLocalWeatherResponse(localWeatherResponse: WeatherResponse): void {
    this.localWeatherResponse = localWeatherResponse;
  }
}
