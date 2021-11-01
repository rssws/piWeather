import {Response} from '../response';
import {Weather} from './weather';
import {Coord} from './coord';
export class WeatherResponse extends Response {
  weather: Weather;
  coord: Coord;
}
