import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild} from '@angular/core';
import {Coord} from '../../model/weather/coord';
import {HourlyWeatherResponse} from '../../model/weather/hourly-weather-response';
import {WeatherService} from '../weather.service';
import {ChartDataSets, ChartOptions, ChartType} from 'chart.js';
import {BaseChartDirective, Color, Label} from 'ng2-charts';
import {finalize} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {WeatherResponse} from '../../model/weather/weather-response';
import {ResponseStatus} from '../../model/response-status.enum';

@Component({
  selector: 'app-weather-hourly-chart',
  templateUrl: './weather-hourly-chart.component.html',
  styleUrls: ['./weather-hourly-chart.component.css']
})
export class WeatherHourlyChartComponent implements OnInit {

  @Input() hourlyWeatherResponse: HourlyWeatherResponse;
  @Input() city: string;
  @Input() coord: Coord;
  @Input() dataLength = 24;
  @Output() notifyHourlyWeatherResponse: EventEmitter<HourlyWeatherResponse> = new EventEmitter<HourlyWeatherResponse>();

  hourlyWeatherResponseLoading = true;
  errorMessage: string;

  constructor(
    private weatherService: WeatherService
  ) { }

  public lineChartData: ChartDataSets[] = [];
  public lineChartLabels: Label[];
  public lineChartOptions: (ChartOptions) = {
    maintainAspectRatio: false,
    responsive: true,
    legend: {
      labels: { fontColor: 'white' }
    },
    scales: {
      xAxes: [{
        ticks: {
          fontColor: 'white',
          fontSize: 11,
          stepSize: 1,
        },
        gridLines: { color: 'rgba(255,255,255,0.1)' }
      }],
      yAxes: [
        {
          id: 'y-axis-0',
          position: 'left',
          ticks: {
            // There is no precision attribute in ng2-charts but in Chart.js. So ignore the warning.
            // @ts-ignore
            precision: 0,
            beginAtZero: true,
            fontColor: 'white',
            fontSize: 15,
            callback: (value, index, values) => {
              return value + '°';
            }
          },
          gridLines: { color: 'rgba(255,255,255,0.1)' }
        },
        {
          id: 'y-axis-1',
          position: 'right',
          ticks: {
            max: 1,
            stepSize: 0.2,
            beginAtZero: true,
            fontColor: 'white',
            fontSize: 10,
            callback: (value, index, values) => {
              return ((value as number) * 100) + '%';
            }
          },
        },
      ]
    },
    layout: {
      padding: {
        left: 0,
        right: 0,
        top: 0,
        bottom: 15
      }
    }
  };

  images: string[];
  public lineChartPlugins = [{
    afterDraw: chart => {
      const ctx = chart.chart.ctx;
      const xAxis = chart.scales['x-axis-0'];
      const yAxis = chart.scales['y-axis-0'];
      xAxis.ticks.forEach((value, index) => {
        const x = xAxis.getPixelForTick(index);
        const image = new Image();
        image.src = this.images[index];
        ctx.drawImage(image, x - 9, yAxis.bottom + 25, 20, 20);
      });
    }
  }];

  public lineChartColors: Color[] = [
    {
      backgroundColor: 'rgba(63,165,255,0.2)',
      borderColor: 'rgb(63,165,255,1)',
      pointBackgroundColor: 'rgb(63,165,255)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgb(63,165,255)'
    },
    {
      backgroundColor: 'rgba(205,229,255,0.6)',
      borderColor: 'rgb(205,229,255)',
      pointBackgroundColor: 'rgba(205,229,255)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(205,229,255)'
    },
  ];
  public lineChartLegend = true;
  public lineChartType: ChartType = 'line';

  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  ngOnInit(): void {
    this.hourlyWeatherResponseLoading = true;
    if (this.hourlyWeatherResponse !== undefined) {
      this.prepareChart(this.hourlyWeatherResponse);
      this.hourlyWeatherResponseLoading = false;
    } else if (this.coord !== undefined) {
      this.updateHourlyWeather(this.coord);
    } else {
      this.errorMessage = '[Error]: coord is empty!';
    }
  }


  updateHourlyWeather(coord: Coord): void {
    const hourlyWeatherResponse$ = this.weatherService.getHourlyWeatherResponseByCoord(coord);
    hourlyWeatherResponse$
      .subscribe(r => {
        if (r.responseStatus.toString() !== 'SUCCESS') {
          this.hourlyWeatherResponseLoading = true;
          this.errorMessage = r.responseMessage;
          return;
        }
        this.hourlyWeatherResponse = r;
        this.notifyHourlyWeatherResponse.emit(r);
        this.prepareChart(r);
        this.hourlyWeatherResponseLoading = false;
      }, error => {
        this.hourlyWeatherResponseLoading = true;
        this.errorMessage = error.message;
        setTimeout(this.updateHourlyWeather.bind(this), 6000);
      });
  }

  prepareChart(hourlyWeatherResponse: HourlyWeatherResponse): void {
    const weathers = hourlyWeatherResponse.hourlyWeather.weathers.slice(0, this.dataLength);
    const temp: ChartDataSets = { data: [], label: 'Temperature' };
    const pop: ChartDataSets = { data: [], label: 'Probability of precipitation', type: 'bar', yAxisID: 'y-axis-1' };
    temp.data = weathers.map(weather => {
      return parseFloat((weather.temp - 273.15).toFixed(1));
    });
    pop.data = weathers.map(weather => {
      return weather.pop;
    });
    this.lineChartLabels = weathers.map((weather, index) => {
      if (index === 0) {
        return 'now';
      }
      return new DatePipe('en-US').transform(weather.dt * 1000 , 'H');
    });

    this.images = weathers.map(weather => {
      return 'https://openweathermap.org/img/wn/' + weather.icon + '.png';
    });
    this.lineChartData.push(temp, pop);
  }
}
