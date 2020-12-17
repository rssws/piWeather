package wang.zhongpin.pi.service.weatherService;

import kong.unirest.*;
import kong.unirest.json.JSONObject;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import wang.zhongpin.pi.model.CachePool;
import wang.zhongpin.pi.model.weather.Weather;
import wang.zhongpin.pi.model.weather.Coord;
import wang.zhongpin.pi.model.weather.WeatherResponse;
import wang.zhongpin.pi.model.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class OpenWeatherAPI extends WeatherAPI {
    @Value("${pi.apiKey.openWeatherAPI}")
    private String apiKey;
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    // set cache valid time to 10 minutes
    private CachePool<WeatherResponse> weatherResponseCachePool = new CachePool<>(1000 * 60 * 10);

    @Override
    public WeatherResponse getResponseByCity(String cityName) throws ExecutionException, InterruptedException, HttpException {
        // if cached
        Map.Entry<Long, WeatherResponse> cache = weatherResponseCachePool.getCache(cityName);
        if(cache != null) {
            WeatherResponse weatherResponse = cache.getValue();
            weatherResponse.responseMessage = "Last time updated: " + new Date(cache.getKey()).toString();
            return weatherResponse;
        }

        Future<HttpResponse<JsonNode>> future = Unirest.get(BASE_URL)
                .queryString("q",cityName)
                .queryString("appid",apiKey)
                .asJsonAsync();
        if(future.get().getStatus() != 200) {
            throw new HttpException("OpenWeatherAPI returns HTTP_CODE " + future.get().getStatus());
        }
        JSONObject r = future.get().getBody().getObject();
        Weather weather = new Weather(
                r.getJSONArray("weather").getJSONObject(0).getString("description"),
                r.getJSONArray("weather").getJSONObject(0).getString("icon"),
                r.getJSONObject("main").getDouble("temp"),
                r.getJSONObject("main").getDouble("temp_max"),
                r.getJSONObject("main").getDouble("temp_min")
        );

        Coord coord = new Coord(
                r.getJSONObject("coord").getDouble("lon"),
                r.getJSONObject("coord").getDouble("lat")
        );

        WeatherResponse ret = new WeatherResponse(ResponseStatus.SUCCESS, weather, coord);
        weatherResponseCachePool.insertCache(cityName, ret);
        return ret;
    }

    @Override
    public WeatherResponse getResponseByCoord(Coord coord) {
        return null;
    }

    @Override
    public WeatherResponse getResponseByZipCode(String zipCode) {
        return null;
    }
}
