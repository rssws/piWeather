package wang.zhongpin.pi.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

        try {
            String remoteAddr = "";
            if (request != null) {
                remoteAddr = request.getHeader("X-FORWARDED-FOR");
                if (remoteAddr == null || "".equals(remoteAddr)) {
                    remoteAddr = request.getRemoteAddr();
                }
            }
            return ipGeolocationAPI.getIPGeolocation(remoteAddr);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/{ip}/{apiKey}")
    public Response getIPGeolocationResponseByIP(
            @PathVariable String ip,
            @PathVariable String apiKey) {
        if(!apiKeyService.validate(apiKey)) {
            return new Response(ResponseStatus.ERROR, "Api key is not valid!");
        }

        try {
            return ipGeolocationAPI.getIPGeolocation(ip);
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

}
