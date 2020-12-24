package wang.zhongpin.pi.service.weatherService;


import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import wang.zhongpin.pi.WeatherApplication;
import wang.zhongpin.pi.model.weather.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class WeatherAPI {

    public abstract WeatherResponse getWeatherResponseByCity(String cityName) throws ExecutionException, InterruptedException, HttpException;
    public abstract WeatherResponse getWeatherResponseByCoord(Coord coord);
    public abstract WeatherResponse getWeatherResponseByZipCode(String zipCode);
    public abstract DailyWeatherResponse getDailyWeatherResponseByCoord(Coord coord) throws ExecutionException, InterruptedException, HttpException;
}

