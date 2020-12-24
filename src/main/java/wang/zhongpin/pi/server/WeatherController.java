package wang.zhongpin.pi.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import wang.zhongpin.pi.model.CachePool;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.model.weather.Coord;
import wang.zhongpin.pi.model.weather.WeatherResponse;
import wang.zhongpin.pi.service.ApiKeyService;
import wang.zhongpin.pi.service.weatherService.OpenWeatherAPI;
import wang.zhongpin.pi.service.weatherService.WeatherAPI;

import static java.lang.Double.parseDouble;

@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/")
public class WeatherController {
    private WeatherAPI weatherAPI;
    private ApiKeyService apiKeyService;

    // autowiring all beans that might be needed later...
    // instead of using field injection, we leave the field just there and initialize it
    // in the constructor, in which we want to inject the bean (not the constructor in bean itself!!!)
    @Autowired
    public WeatherController(
            OpenWeatherAPI openWeatherAPI,
            ApiKeyService apiKeyService) {
        this.weatherAPI = openWeatherAPI;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/weather/city/{cityName}/{apiKey}")
    public Response getWeatherResponse(
            @PathVariable String cityName,
            @PathVariable String apiKey) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }

        try {
            return weatherAPI.getWeatherResponseByCity(cityName);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/dailyWeather/coord/{lat}/{lon}/{apiKey}")
    public Response getDailyWeatherResponse(
            @PathVariable String lat,
            @PathVariable String lon,
            @PathVariable String apiKey) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }

        try {
            return weatherAPI.getDailyWeatherResponseByCoord(new Coord(parseDouble(lat), parseDouble(lon)));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

}
