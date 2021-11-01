import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {ChartData, ChartDataSets, ChartLineOptions, ChartOptions, ChartType} from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import {Coord} from '../../model/weather/coord';
import {DailyWeatherResponse} from '../../model/weather/daily-weather-response';
import {WeatherService} from '../weather.service';
import {finalize} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {WeatherResponse} from '../../model/weather/weather-response';
import {ResponseStatus} from '../../model/response-status.enum';

@Component({
  selector: 'app-weather-seven-day-chart',
  templateUrl: './weather-seven-day-chart.component.html',
  styleUrls: ['./weather-seven-day-chart.component.css']
})
export class WeatherSevenDayChartComponent implements OnInit {

  @Input() city: string;
  @Input() coord: Coord;
  @Input() dailyWeatherResponse: DailyWeatherResponse;
  @Output() notifyDailyWeatherResponse: EventEmitter<DailyWeatherResponse> = new EventEmitter<DailyWeatherResponse>();

  dailyWeatherResponseLoading = true;
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
        ticks: { fontColor: 'white', fontSize: 15 },
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
        bottom: 30
      }
    },
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
        image.src = this.images[index],
          ctx.drawImage(image, x - 22.5, yAxis.bottom + 25, 45, 45);
      });
    }
  }];

  public lineChartColors: Color[] = [
    {
      backgroundColor: 'rgba(63,165,255,0.2)',
      borderColor: 'rgba(63,165,255,1)',
      pointBackgroundColor: 'rgba(63,165,255,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(63,165,255,1)'
    },
    {
      backgroundColor: 'rgba(255,182,0,0.2)',
      borderColor: 'rgb(255,182,0)',
      pointBackgroundColor: 'rgba(255,182,0,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(255,182,0,1)'
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
    this.dailyWeatherResponseLoading = true;
    if (this.dailyWeatherResponse !== undefined) {
      this.prepareChart(this.dailyWeatherResponse);
      this.dailyWeatherResponseLoading = false;
    } else if (this.coord !== undefined) {
      this.updateDailyWeather(this.coord);
    } else {
      this.errorMessage = '[Error]: coord is empty!';
    }
  }

  updateDailyWeather(coord: Coord): void {
    const dailyWeatherResponse$ = this.weatherService.getDailyWeatherResponseByCoord(coord);
    dailyWeatherResponse$
      .subscribe(r => {
        if (r.responseStatus.toString() !== 'SUCCESS') {
          this.dailyWeatherResponseLoading = true;
          this.errorMessage = r.responseMessage;
          return;
        }
        this.dailyWeatherResponse = r;
        this.notifyDailyWeatherResponse.emit(r);
        this.prepareChart(r);
        this.dailyWeatherResponseLoading = false;
        this.errorMessage = undefined;
      }, error => {
        this.dailyWeatherResponseLoading = true;
        this.errorMessage = error.message;
        setTimeout(this.updateDailyWeather.bind(this), 6000);
      });
  }

  prepareChart(dailyWeatherResponse: DailyWeatherResponse): void {
    const tempMax: ChartDataSets = { data: [], label: 'max', fill: 'origin' };
    const tempMin: ChartDataSets = { data: [], label: 'min', fill: 'origin' };
    const pop: ChartDataSets = { data: [], label: 'Probability of precipitation', type: 'bar', yAxisID: 'y-axis-1' };
    tempMax.data = dailyWeatherResponse.dailyWeather.weathers.map(weather => {
      return parseFloat((weather.tempMax - 273.15).toFixed(1));
    });
    tempMin.data = dailyWeatherResponse.dailyWeather.weathers.map(weather => {
      return parseFloat((weather.tempMin - 273.15).toFixed(1));
    });
    pop.data = dailyWeatherResponse.dailyWeather.weathers.map(weather => {
      return weather.pop;
    });
    this.lineChartLabels = dailyWeatherResponse.dailyWeather.weathers.map((weather, index) => {
      if (index === 0) {
        return 'Today';
      }
      return new DatePipe('en-US').transform(weather.dt * 1000 , 'E');
    });

    this.images = dailyWeatherResponse.dailyWeather.weathers.map(weather => {
      return 'https://openweathermap.org/img/wn/' + weather.icon + '.png';
    });
    this.lineChartData.push(tempMin, tempMax, pop);
  }
}
