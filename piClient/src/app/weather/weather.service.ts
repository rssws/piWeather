import { Observable } from 'rxjs';
import { WeatherResponse } from '../model/weather/weather-response';
import { DailyWeatherResponse } from '../model/weather/daily-weather-response';
import { HttpClient } from '@angular/common/http';
import { Coord } from '../model/weather/coord';
import { Injectable } from '@angular/core';
import { HourlyWeatherResponse } from '../model/weather/hourly-weather-response';

@Injectable({
  providedIn: 'root',
})
export class WeatherService {
  private baseUrl = process.env.NODE_ENV === 'production' ? '/api/' : 'http://localhost:31415/';

  constructor(private http: HttpClient) {}

  public getWeatherResponseByCity(city: string): Observable<WeatherResponse> {
    return this.http.get<WeatherResponse>(this.baseUrl + 'weather/city/' + city);
  }

  public getDailyWeatherResponseByCoord(coord: Coord): Observable<DailyWeatherResponse> {
    return this.http.get<DailyWeatherResponse>(this.baseUrl + 'weather/daily/coord/' + coord.lat + '/' + coord.lon);
  }

  public getHourlyWeatherResponseByCoord(coord: Coord): Observable<HourlyWeatherResponse> {
    return this.http.get<HourlyWeatherResponse>(this.baseUrl + 'weather/hourly/coord/' + coord.lat + '/' + coord.lon);
  }

  public getLocationByIP(ipAddr = ''): Observable<string> {
    if (ipAddr === '') {
      return this.http.get<string>(this.baseUrl + 'ipGeolocation/');
    } else {
      return this.http.get<string>(this.baseUrl + 'ipGeolocation/' + ipAddr);
    }
  }
}
