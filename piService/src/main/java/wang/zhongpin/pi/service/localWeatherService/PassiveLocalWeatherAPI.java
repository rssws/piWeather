package wang.zhongpin.pi.service.localWeatherService;

import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;
import wang.zhongpin.pi.model.CachePool;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.model.weather.Weather;
import wang.zhongpin.pi.model.weather.WeatherResponse;

@Component
public class PassiveLocalWeatherAPI extends LocalWeatherAPI {
    // local weather valid for 5 min
    private final CachePool<WeatherResponse> localWeatherResponseCachePool = new CachePool<>(1000 * 60 * 5);

    @Override
    public Response getLocalWeatherByName(String name) {
        // if cached
        Map.Entry<Long, WeatherResponse> cache = localWeatherResponseCachePool.getCache(name);
        if(cache != null) {
            WeatherResponse weatherResponse = cache.getValue();
            weatherResponse.responseMessage = "Last updated: " + new Date(cache.getKey()).toString();
            return weatherResponse;
        }

        return new Response(ResponseStatus.ERROR, "Local weather \"" + name + "\" not found!");
    }

    @Override
    public void setLocalWeatherByName(String name, Weather weather) {
        WeatherResponse weatherResponse = new WeatherResponse(ResponseStatus.SUCCESS, weather, null, name);
        localWeatherResponseCachePool.insertCache(name, weatherResponse);
    }
}
