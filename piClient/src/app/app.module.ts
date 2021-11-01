import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { WeatherComponent } from './weather/weather.component';
import { NavigationComponent } from './navigation/navigation.component';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { WeatherDefaultComponent } from './weather/weather-default/weather-default.component';
import { ChartsModule } from 'ng2-charts';
import { WeatherSevenDayChartComponent } from './weather/weather-seven-day-chart/weather-seven-day-chart.component';
import { WeatherHourlyChartComponent } from './weather/weather-hourly-chart/weather-hourly-chart.component';

@NgModule({
  declarations: [
    AppComponent,
    WeatherComponent,
    NavigationComponent,
    WeatherDefaultComponent,
    WeatherSevenDayChartComponent,
    WeatherHourlyChartComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    ChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
