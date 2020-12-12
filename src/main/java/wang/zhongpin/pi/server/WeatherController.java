package wang.zhongpin.pi.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.service.ApiKeyService;
import wang.zhongpin.pi.service.weatherService.OpenWeatherAPI;
import wang.zhongpin.pi.service.weatherService.WeatherAPI;

@RestController
@RequestMapping("/weather")
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

    @GetMapping("/city/{cityName}/{apiKey}")
    public Response getWeatherResponse(
            @PathVariable String cityName,
            @PathVariable String apiKey) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }


        try {
            return weatherAPI.getResponseByCity(cityName);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

}
