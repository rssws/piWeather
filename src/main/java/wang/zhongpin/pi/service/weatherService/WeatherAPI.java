package wang.zhongpin.pi.service.weatherService;


import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import wang.zhongpin.pi.WeatherApplication;
import wang.zhongpin.pi.model.weather.Coord;
import wang.zhongpin.pi.model.weather.Weather;
import wang.zhongpin.pi.model.weather.WeatherResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class WeatherAPI {

    public abstract WeatherResponse getResponseByCity(String cityName) throws ExecutionException, InterruptedException, HttpException;
    public abstract WeatherResponse getResponseByCoord(Coord coord);
    public abstract WeatherResponse getResponseByZipCode(String zipCode);
}

