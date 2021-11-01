import {Response} from '../response';
import {Coord} from './coord';
import {HourlyWeather} from './hourly-weather';

export class HourlyWeatherResponse extends Response {
  hourlyWeather: HourlyWeather;
  coord: Coord;
}
