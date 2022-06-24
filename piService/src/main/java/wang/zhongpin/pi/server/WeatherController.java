package wang.zhongpin.pi.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.zhongpin.pi.model.RequestLimitation;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.model.weather.Coord;
import wang.zhongpin.pi.service.ApiKeyService;
import wang.zhongpin.pi.service.weatherService.OpenWeatherAPI;
import wang.zhongpin.pi.service.weatherService.WeatherAPI;
import javax.servlet.http.HttpServletRequest;
import static java.lang.Double.parseDouble;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherAPI weatherAPI;

    RequestLimitation requestLimitationNormal = new RequestLimitation(1000 * 60, 20);
    RequestLimitation requestLimitationSensitive = new RequestLimitation(1000 * 60, 10);
    RequestLimitation requestLimitationSensitive2 = new RequestLimitation(1000 * 60, 5);

    @Autowired
    public WeatherController(
            OpenWeatherAPI openWeatherAPI,
            ApiKeyService apiKeyService) {
        this.weatherAPI = openWeatherAPI;
    }

    @GetMapping("/city/{cityName}")
    public Response getWeatherResponseByCity(
            @PathVariable String cityName,
            HttpServletRequest request) {
        if (requestLimitationNormal.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getWeatherResponseByCity(cityName);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/daily/coord/{lat}/{lon}")
    public Response getDailyWeatherResponseByCoord(
            @PathVariable String lat,
            @PathVariable String lon,
            HttpServletRequest request) {
        if (requestLimitationSensitive.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getDailyWeatherResponseByCoord(new Coord(parseDouble(lat), parseDouble(lon)));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/hourly/coord/{lat}/{lon}")
    public Response getHourlyWeatherResponseByCoord(
            @PathVariable String lat,
            @PathVariable String lon,
            HttpServletRequest request) {
        if (requestLimitationSensitive.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getHourlyWeatherResponseByCoord(new Coord(parseDouble(lat), parseDouble(lon)));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/ip")
    public Response getWeatherResponseByIP(
            HttpServletRequest request) {
        if (requestLimitationNormal.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return weatherAPI.getWeatherResponseByIP(Utils.getRemoteAddr(request));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/ip/{ip}")
    public Response getWeatherResponseByIP(
            @PathVariable String ip,
            HttpServletRequest request) {
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
