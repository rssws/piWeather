package wang.zhongpin.pi.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.zhongpin.pi.model.RequestLimitation;
import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;
import wang.zhongpin.pi.service.IPGeolocationService.IPGeolocationAPI;
import wang.zhongpin.pi.service.IPGeolocationService.IpApiComIPGeolocationAPI;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ipGeolocation")
public class IPGeolocationController {
    private final IPGeolocationAPI ipGeolocationAPI;

    RequestLimitation requestLimitationNormal = new RequestLimitation(1000 * 60, 30);
    RequestLimitation requestLimitationSensitive = new RequestLimitation(1000 * 60, 5);

    @Autowired
    public IPGeolocationController(
            IpApiComIPGeolocationAPI ipApiComIPGeolocationAPI) {
        this.ipGeolocationAPI = ipApiComIPGeolocationAPI;
    }

    @GetMapping("/")
    public Response getIPGeolocationResponse(HttpServletRequest request) {
        if (requestLimitationNormal.isTooFrequent(Utils.getRemoteAddr(request))) {
            return new Response(ResponseStatus.ERROR, "Request sent too frequently! Please wait for one minute!");
        }

        try {
            return ipGeolocationAPI.getIPGeolocation(Utils.getRemoteAddr(request));
        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @GetMapping("/{ip}")
    public Response getIPGeolocationResponseByIP(
            @PathVariable String ip,
            HttpServletRequest request) {
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
