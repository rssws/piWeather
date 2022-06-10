import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { Coord } from '../../model/weather/coord';
import { HourlyWeatherResponse } from '../../model/weather/hourly-weather-response';
import { WeatherService } from '../weather.service';
import { ChartConfiguration, ChartDataset, ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-weather-hourly-chart',
  templateUrl: './weather-hourly-chart.component.html',
  styleUrls: ['./weather-hourly-chart.component.css'],
})
export class WeatherHourlyChartComponent implements OnInit {
  @Input() hourlyWeatherResponse: HourlyWeatherResponse;
  @Input() city: string;
  @Input() coord: Coord;
  @Input() dataLength = 24;
  @Output() notifyHourlyWeatherResponse: EventEmitter<HourlyWeatherResponse> =
    new EventEmitter<HourlyWeatherResponse>();

  hourlyWeatherResponseLoading = true;
  errorMessage: string;

  constructor(private weatherService: WeatherService) {}

  public lineChartData: ChartConfiguration['data'] = { datasets: [], labels: [] };
  public lineChartOptions: ChartConfiguration['options'] = {
    elements: {
      line: {
        tension: 0.3,
      },
    },
    maintainAspectRatio: false,
    responsive: true,
    scales: {
      x: {
        ticks: {
          color: 'white',
          font: {
            size: 11,
          },
          stepSize: 1,
        },
        grid: { color: 'rgba(255,255,255,0.1)', offset: false },
      },
      'y-axis-0': {
        position: 'left',
        beginAtZero: true,
        ticks: {
          precision: 0,
          color: 'white',
          font: {
            size: 15,
          },
          callback: (value, index, values) => value + '°',
        },
        grid: { color: 'rgba(255,255,255,0.1)' },
      },
      'y-axis-1': {
        position: 'right',
        beginAtZero: true,
        max: 1,
        ticks: {
          stepSize: 0.2,
          color: 'white',
          font: { size: 10 },
          callback: (value, index, values) => (value as number) * 100 + '%',
        },
      },
    },
    layout: {
      padding: {
        left: 0,
        right: 0,
        top: 0,
        bottom: 15,
      },
    },
    plugins: {
      legend: {
        labels: { color: 'white' },
      },
    },
  };

  images: string[];
  public lineChartPlugins = [
    {
      afterDraw: (chart) => {
        const ctx = chart.ctx;
        const xAxis = chart.scales['x'];
        const yAxis = chart.scales['y-axis-0'];
        xAxis.ticks.forEach((value, index) => {
          const x = xAxis.getPixelForTick(index);
          const image = new Image();
          image.src = this.images[index];
          ctx.drawImage(image, x - 9, yAxis.bottom + 25, 20, 20);
        });
      },
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
    hourlyWeatherResponse$.subscribe(
      (r) => {
        if (r.responseStatus.toString() !== 'SUCCESS') {
          this.hourlyWeatherResponseLoading = true;
          this.errorMessage = r.responseMessage;
          return;
        }
        this.hourlyWeatherResponse = r;
        this.notifyHourlyWeatherResponse.emit(r);
        this.prepareChart(r);
        this.hourlyWeatherResponseLoading = false;
      },
      (error) => {
        this.hourlyWeatherResponseLoading = true;
        this.errorMessage = error.message;
        setTimeout(this.updateHourlyWeather.bind(this), 6000);
      }
    );
  }

  prepareChart(hourlyWeatherResponse: HourlyWeatherResponse): void {
    const weathers = hourlyWeatherResponse.hourlyWeather.weathers.slice(0, this.dataLength);
    const temp: ChartDataset = {
      data: [],
      label: 'Temperature',
      backgroundColor: 'rgba(63,165,255,0.2)',
      borderColor: 'rgb(63,165,255,1)',
      hoverBackgroundColor: '#fff',
      hoverBorderColor: 'rgb(63,165,255)',
      pointBackgroundColor: 'rgb(63,165,255)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgb(63,165,255)',
    };
    const pop: ChartDataset = {
      data: [],
      label: 'Probability of precipitation',
      type: 'bar',
      yAxisID: 'y-axis-1',
      backgroundColor: 'rgba(205,229,255,0.6)',
      borderColor: 'rgb(205,229,255)',
      hoverBackgroundColor: '#fff',
      hoverBorderColor: 'rgba(205,229,255)',
    };

    temp.data = weathers.map((weather) => parseFloat((weather.temp - 273.15).toFixed(1)));
    pop.data = weathers.map((weather) => weather.pop);
    const lineChartLabels = weathers.map((weather, index) => {
      if (index === 0) {
        return 'now';
      }
      return new DatePipe('en-US').transform(weather.dt * 1000, 'H');
    });

    this.images = weathers.map((weather) => 'https://openweathermap.org/img/wn/' + weather.icon + '.png');
    this.lineChartData.datasets.push(temp, pop);
    this.lineChartData.labels.push(...lineChartLabels);
  }
}
