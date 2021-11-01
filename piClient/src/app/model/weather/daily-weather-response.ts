import {Response} from '../response';
import {Coord} from './coord';
import {DailyWeather} from './daily-weather';

export class DailyWeatherResponse extends Response {
  dailyWeather: DailyWeather;
  coord: Coord;
}
