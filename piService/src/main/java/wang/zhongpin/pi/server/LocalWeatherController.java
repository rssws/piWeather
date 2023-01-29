package wang.zhongpin.pi.server;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wang.zhongpin.pi.model.RequestLimitation;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.model.weather.Weather;
import wang.zhongpin.pi.service.ApiKeyService;
import wang.zhongpin.pi.service.localWeatherService.LocalWeatherAPI;
import wang.zhongpin.pi.service.localWeatherService.PassiveLocalWeatherAPI;

@RestController
@RequestMapping("/local-weather")
public class LocalWeatherController {
    private final LocalWeatherAPI localWeatherAPI;
    private final ApiKeyService apiKeyService;

    RequestLimitation requestLimitationNormal = new RequestLimitation(1000 * 60, 20);

    @Autowired
    public LocalWeatherController(
            PassiveLocalWeatherAPI passiveLocalWeatherAPI,
            ApiKeyService apiKeyService) {
        this.localWeatherAPI = passiveLocalWeatherAPI;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/{name}")
    public Response getLocalWeatherResponse(
            @PathVariable String name,
            HttpServletRequest request) {
        if (requestLimitationNormal.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        return localWeatherAPI.getLocalWeatherByName(name);
    }

    @PutMapping("/{name}/{apiKey}")
    public Response updateLocalWeatheResponse(
            @RequestBody Weather weather,
            @PathVariable String name,
            @PathVariable String apiKey,
            HttpServletRequest request) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }

        localWeatherAPI.setLocalWeatherByName(name, weather);
        return localWeatherAPI.getLocalWeatherByName(name);
    }
}
