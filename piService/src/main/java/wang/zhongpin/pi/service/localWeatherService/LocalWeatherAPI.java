package wang.zhongpin.pi.service.localWeatherService;

import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.weather.Weather;

public abstract class LocalWeatherAPI {
    public abstract Response getLocalWeatherByName(String name);
    public abstract void setLocalWeatherByName(String name, Weather weather);
}
