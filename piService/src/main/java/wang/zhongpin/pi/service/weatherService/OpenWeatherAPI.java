package wang.zhongpin.pi.service.weatherService;

import kong.unirest.*;
import kong.unirest.json.JSONObject;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wang.zhongpin.pi.model.CachePool;
import wang.zhongpin.pi.model.weather.*;
import wang.zhongpin.pi.model.ResponseStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class OpenWeatherAPI extends WeatherAPI {
    @Value("${pi.apiKey.openWeatherAPI}")
    private String apiKey;
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    // weather: cache valid time <- 10 minutes
    private final CachePool<WeatherResponse> weatherResponseCachePool = new CachePool<>(1000 * 60 * 10);
    // daily weather: cache valid time <- 1 hour
    private final CachePool<DailyWeatherResponse> dailyWeatherResponseCachePool = new CachePool<>(1000 * 60 * 60);
    // hourly weather: cache valid time <- 30 minutes
    private final CachePool<HourlyWeatherResponse> hourlyWeatherResponseCachePool = new CachePool<>(1000 * 60 * 30);

    @Override
    public WeatherResponse getWeatherResponseByCity(String cityName) throws ExecutionException, InterruptedException, HttpException {
        // if cached
        Map.Entry<Long, WeatherResponse> cache = weatherResponseCachePool.getCache(cityName);
        if(cache != null) {
            WeatherResponse weatherResponse = cache.getValue();
            weatherResponse.responseMessage = "Last time updated: " + new Date(cache.getKey()).toString();
            return weatherResponse;
        }

        Future<HttpResponse<JsonNode>> future = Unirest.get(BASE_URL + "weather")
                .queryString("q", cityName)
                .queryString("appid", apiKey)
                .asJsonAsync();
        if(future.get().getStatus() != 200) {
            throw new HttpException("OpenWeatherAPI returns HTTP_CODE " + future.get().getStatus());
        }
        JSONObject r = future.get().getBody().getObject();
        Weather weather = new Weather(
                r.getInt("dt"),
                r.getJSONArray("weather").getJSONObject(0).getString("description"),
                r.getJSONArray("weather").getJSONObject(0).getString("icon"),
                r.getJSONObject("main").getDouble("temp"),
                r.getJSONObject("main").getDouble("temp_max"),
                r.getJSONObject("main").getDouble("temp_min"),
                r.getJSONObject("main").getDouble("feels_like"),
                r.getJSONObject("main").getDouble("pressure"),
                r.getJSONObject("main").getDouble("humidity"),
                r.getJSONObject("wind").getDouble("speed"),
                r.getJSONObject("wind").getDouble("deg"),
                null);

        Coord coord = new Coord(
                r.getJSONObject("coord").getDouble("lat"),
                r.getJSONObject("coord").getDouble("lon")
        );

        String name = r.getString("name");

        WeatherResponse ret = new WeatherResponse(ResponseStatus.SUCCESS, weather, coord, name);
        weatherResponseCachePool.insertCache(cityName, ret);
        return ret;
    }

    @Override
    public WeatherResponse getWeatherResponseByCoord(Coord coord) {
        return null;
    }

    @Override
    public WeatherResponse getWeatherResponseByZipCode(String zipCode) {
        return null;
    }

    @Override
    public DailyWeatherResponse getDailyWeatherResponseByCoord(Coord coord) throws ExecutionException, InterruptedException, HttpException {
        // if cached
        Map.Entry<Long, DailyWeatherResponse> cache = dailyWeatherResponseCachePool.getCache(coord.toString());
        if(cache != null) {
            DailyWeatherResponse dailyWeatherResponse = cache.getValue();
            dailyWeatherResponse.responseMessage = "Last time updated: " + new Date(cache.getKey()).toString();
            return dailyWeatherResponse;
        }

        // use the free onecall api provided by openweathermap.org
        System.out.println("lat:" + coord.getLat());
        System.out.println("lat:" + coord.getLon());

        Future<HttpResponse<JsonNode>> future = Unirest.get(BASE_URL + "onecall")
                .queryString("lat", coord.getLat())
                .queryString("lon", coord.getLon())
                .queryString("exclude", "current,minutely,hourly,alerts")
                .queryString("appid", apiKey)
                .asJsonAsync();
        if(future.get().getStatus() != 200) {
            throw new HttpException(
                    "OpenWeatherAPI returns HTTP_CODE " + future.get().getStatus() + ". "
                            + "ResponseBody:" + future.get().getBody().toString()
            );
        }
        JSONObject r = future.get().getBody().getObject();
        int dailyWeatherLength = r.getJSONArray("daily").length();
        DailyWeather dailyWeather = new DailyWeather(new ArrayList<>());
        for(int i = 0; i < dailyWeatherLength; i++) {
            JSONObject tmp = r.getJSONArray("daily").getJSONObject(i);
            Weather weather = new Weather(
                    tmp.getInt("dt"),
                    tmp.getJSONArray("weather").getJSONObject(0).getString("description"),
                    tmp.getJSONArray("weather").getJSONObject(0).getString("icon"),
                    null,
                    tmp.getJSONObject("temp").getDouble("max"),
                    tmp.getJSONObject("temp").getDouble("min"),
                    null,
                    tmp.getDouble("pressure"),
                    tmp.getDouble("humidity"),
                    tmp.getDouble("wind_speed"),
                    tmp.getDouble("wind_deg"),
                    tmp.getDouble("pop"));
            dailyWeather.addWeather(weather);
        }

        DailyWeatherResponse ret = new DailyWeatherResponse(ResponseStatus.SUCCESS, dailyWeather, coord);
        dailyWeatherResponseCachePool.insertCache(coord.toString(), ret);
        return ret;
    }

    @Override
    public HourlyWeatherResponse getHourlyWeatherResponseByCoord(Coord coord) throws ExecutionException, InterruptedException, HttpException {
        // if cached
        Map.Entry<Long, HourlyWeatherResponse> cache = hourlyWeatherResponseCachePool.getCache(coord.toString());
        if(cache != null) {
            HourlyWeatherResponse hourlyWeatherResponse = cache.getValue();
            hourlyWeatherResponse.responseMessage = "Last time updated: " + new Date(cache.getKey()).toString();
            return hourlyWeatherResponse;
        }

        // use the free onecall api provided by openweathermap.org
        System.out.println("lat:" + coord.getLat());
        System.out.println("lat:" + coord.getLon());

        Future<HttpResponse<JsonNode>> future = Unirest.get(BASE_URL + "onecall")
                .queryString("lat", coord.getLat())
                .queryString("lon", coord.getLon())
                .queryString("exclude", "current,minutely,daily,alerts")
                .queryString("appid", apiKey)
                .asJsonAsync();
        if(future.get().getStatus() != 200) {
            throw new HttpException(
                    "OpenWeatherAPI returns HTTP_CODE " + future.get().getStatus() + ". "
                            + "ResponseBody:" + future.get().getBody().toString()
            );
        }
        JSONObject r = future.get().getBody().getObject();
        int hourlyWeatherLength = r.getJSONArray("hourly").length();
        HourlyWeather hourlyWeather = new HourlyWeather(new ArrayList<>());
        for(int i = 0; i < hourlyWeatherLength; i++) {
            JSONObject tmp = r.getJSONArray("hourly").getJSONObject(i);
            Weather weather = new Weather(
                    tmp.getInt("dt"),
                    tmp.getJSONArray("weather").getJSONObject(0).getString("description"),
                    tmp.getJSONArray("weather").getJSONObject(0).getString("icon"),
                    tmp.getDouble("temp"),
                    null,
                    null,
                    tmp.getDouble("feels_like"),
                    tmp.getDouble("pressure"),
                    tmp.getDouble("humidity"),
                    tmp.getDouble("wind_speed"),
                    tmp.getDouble("wind_deg"),
                    tmp.getDouble("pop"));
            hourlyWeather.addWeather(weather);
        }

        HourlyWeatherResponse ret = new HourlyWeatherResponse(ResponseStatus.SUCCESS, hourlyWeather, coord);
        hourlyWeatherResponseCachePool.insertCache(coord.toString(), ret);
        return ret;
    }


}
