package wang.zhongpin.pi.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.zhongpin.pi.model.RequestLimitation;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.service.ApiKeyService;
import wang.zhongpin.pi.service.IPGeolocationService.IPGeolocationAPI;
import wang.zhongpin.pi.service.IPGeolocationService.IpApiComIPGeolocationAPI;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/ipGeolocation")
public class IPGeolocationController {
    private final IPGeolocationAPI ipGeolocationAPI;
    private final ApiKeyService apiKeyService;

    RequestLimitation requestLimitationNormal = new RequestLimitation(1000 * 60, 30);
    RequestLimitation requestLimitationSensitive = new RequestLimitation(1000 * 60, 5);

    @Autowired
    public IPGeolocationController(
            IpApiComIPGeolocationAPI ipApiComIPGeolocationAPI,
            ApiKeyService apiKeyService) {
        this.ipGeolocationAPI = ipApiComIPGeolocationAPI;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/{apiKey}")
    public Response getIPGeolocationResponse(@PathVariable String apiKey, HttpServletRequest request) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }
        if (requestLimitationNormal.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return ipGeolocationAPI.getIPGeolocation(Utils.getRemoteAddr(request));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/{ip}/{apiKey}")
    public Response getIPGeolocationResponseByIP(
            @PathVariable String ip,
            @PathVariable String apiKey,
            HttpServletRequest request) {

        if (!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }
        if (requestLimitationSensitive.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return ipGeolocationAPI.getIPGeolocation(ip);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

}
