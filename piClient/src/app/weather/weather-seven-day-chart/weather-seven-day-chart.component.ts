import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ChartConfiguration, ChartDataset, ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { Coord } from '../../model/weather/coord';
import { DailyWeatherResponse } from '../../model/weather/daily-weather-response';
import { WeatherService } from '../weather.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-weather-seven-day-chart',
  templateUrl: './weather-seven-day-chart.component.html',
  styleUrls: ['./weather-seven-day-chart.component.css'],
})
export class WeatherSevenDayChartComponent implements OnInit {
  @Input() city: string;
  @Input() coord: Coord;
  @Input() dailyWeatherResponse: DailyWeatherResponse;
  @Output() notifyDailyWeatherResponse: EventEmitter<DailyWeatherResponse> = new EventEmitter<DailyWeatherResponse>();

  dailyWeatherResponseLoading = true;
  errorMessage: string;

  constructor(private weatherService: WeatherService) {}

  public lineChartData: ChartConfiguration['data'] = { datasets: [], labels: [] };
  public lineChartOptions: ChartOptions = {
    elements: {
      line: {
        tension: 0.3,
      },
    },
    maintainAspectRatio: false,
    responsive: true,
    scales: {
      x: {
        offset: false,
        ticks: { color: 'white', font: { size: 15 } },
        grid: { color: 'rgba(255,255,255,0.1)' },
      },
      'y-axis-0': {
        position: 'left',
        beginAtZero: true,
        ticks: {
          precision: 0,
          color: 'white',
          font: { size: 15 },
          callback: (value, index, values) => value + '°',
        },
        grid: { color: 'rgba(255,255,255,0.1)' },
      },
      'y-axis-1': {
        position: 'right',
        max: 1,
        beginAtZero: true,
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
        bottom: 30,
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
          (image.src = this.images[index]), ctx.drawImage(image, x - 22.5, yAxis.bottom + 25, 45, 45);
        });
      },
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
    dailyWeatherResponse$.subscribe(
      (r) => {
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
      },
      (error) => {
        this.dailyWeatherResponseLoading = true;
        this.errorMessage = error.message;
        setTimeout(this.updateDailyWeather.bind(this), 6000);
      }
    );
  }

  prepareChart(dailyWeatherResponse: DailyWeatherResponse): void {
    const tempMax: ChartDataset = {
      data: [],
      label: 'max',
      fill: 'origin',
      backgroundColor: 'rgba(63,165,255,0.2)',
      borderColor: 'rgba(63,165,255,1)',
      pointBackgroundColor: 'rgba(63,165,255,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(63,165,255,1)',
    };
    const tempMin: ChartDataset = {
      data: [],
      label: 'min',
      fill: 'origin',
      backgroundColor: 'rgba(255,182,0,0.2)',
      borderColor: 'rgb(255,182,0)',
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
    tempMax.data = dailyWeatherResponse.dailyWeather.weathers.map((weather) =>
      parseFloat((weather.tempMax - 273.15).toFixed(1))
    );
    tempMin.data = dailyWeatherResponse.dailyWeather.weathers.map((weather) =>
      parseFloat((weather.tempMin - 273.15).toFixed(1))
    );
    pop.data = dailyWeatherResponse.dailyWeather.weathers.map((weather) => weather.pop);
    const lineChartLabels = dailyWeatherResponse.dailyWeather.weathers.map((weather, index) => {
      if (index === 0) {
        return 'Today';
      }
      return new DatePipe('en-US').transform(weather.dt * 1000, 'E');
    });

    this.images = dailyWeatherResponse.dailyWeather.weathers.map(
      (weather) => 'https://openweathermap.org/img/wn/' + weather.icon + '.png'
    );
    this.lineChartData.datasets.push(tempMin, tempMax, pop);
    this.lineChartData.labels.push(...lineChartLabels);
  }
}
