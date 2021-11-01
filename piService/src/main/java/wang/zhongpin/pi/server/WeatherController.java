package wang.zhongpin.pi.server;

import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import wang.zhongpin.pi.model.CachePool;
import wang.zhongpin.pi.model.RequestLimitation;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.model.weather.Coord;
import wang.zhongpin.pi.model.weather.WeatherResponse;
import wang.zhongpin.pi.service.ApiKeyService;
import wang.zhongpin.pi.service.weatherService.OpenWeatherAPI;
import wang.zhongpin.pi.service.weatherService.WeatherAPI;

import javax.servlet.http.HttpServletRequest;

import static java.lang.Double.parseDouble;

@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/")
public class WeatherController {
    private final WeatherAPI weatherAPI;
    private final ApiKeyService apiKeyService;

    RequestLimitation requestLimitationNormal = new RequestLimitation(1000 * 60, 20);
    RequestLimitation requestLimitationSensitive = new RequestLimitation(1000 * 60, 10);
    RequestLimitation requestLimitationSensitive2 = new RequestLimitation(1000 * 60, 5);

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
    public Response getWeatherResponseByCity(
            @PathVariable String cityName,
            @PathVariable String apiKey,
            HttpServletRequest request) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }
        if (requestLimitationNormal.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getWeatherResponseByCity(cityName);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/dailyWeather/coord/{lat}/{lon}/{apiKey}")
    public Response getDailyWeatherResponseByCoord(
            @PathVariable String lat,
            @PathVariable String lon,
            @PathVariable String apiKey,
            HttpServletRequest request) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }
        if (requestLimitationSensitive.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getDailyWeatherResponseByCoord(new Coord(parseDouble(lat), parseDouble(lon)));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/hourlyWeather/coord/{lat}/{lon}/{apiKey}")
    public Response getHourlyWeatherResponseByCoord(
            @PathVariable String lat,
            @PathVariable String lon,
            @PathVariable String apiKey,
            HttpServletRequest request) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }
        if (requestLimitationSensitive.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }


        try {
            return weatherAPI.getHourlyWeatherResponseByCoord(new Coord(parseDouble(lat), parseDouble(lon)));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/weather/ip/{apiKey}")
    public Response getWeatherResponseByIP(
            @PathVariable String apiKey,
            HttpServletRequest request) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }
        if (requestLimitationNormal.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getWeatherResponseByIP(Utils.getRemoteAddr(request));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/weather/ip/{ip}/{apiKey}")
    public Response getWeatherResponseByIP(
            @PathVariable String ip,
            @PathVariable String apiKey,
            HttpServletRequest request) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }
        if (requestLimitationSensitive2.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getWeatherResponseByIP(ip);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }
}
