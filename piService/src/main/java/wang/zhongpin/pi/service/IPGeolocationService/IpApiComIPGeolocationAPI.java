package wang.zhongpin.pi.service.IPGeolocationService;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.apache.http.HttpException;
import org.springframework.stereotype.Component;
import wang.zhongpin.pi.model.CachePool;
import wang.zhongpin.pi.model.IPGeolocation.IPGeolocation;
import wang.zhongpin.pi.model.IPGeolocation.IPGeolocationResponse;
import wang.zhongpin.pi.model.ResponseStatus;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This implementation uses data provided by ip-api.com.
 */
@Component
public class IpApiComIPGeolocationAPI extends IPGeolocationAPI {
    private final String BASE_URL = "http://ip-api.com/json/";
    // ip location: cache valid time <- 1 day
    private final CachePool<IPGeolocationResponse> ipGeolocationResponseCachePool = new CachePool<>(1000 * 60 * 60 * 24);

    @Override
    public IPGeolocationResponse getIPGeolocation() throws InterruptedException, ExecutionException, HttpException {
        return getIPGeolocation("");
    }

    @Override
    public IPGeolocationResponse getIPGeolocation(String ipAddr) throws ExecutionException, InterruptedException, HttpException {
        if(!ipAddr.equals("")) {
            // if cached
            Map.Entry<Long, IPGeolocationResponse> cache = ipGeolocationResponseCachePool.getCache(ipAddr);
            if(cache != null) {
                IPGeolocationResponse ipGeolocationResponse = cache.getValue();
                ipGeolocationResponse.responseMessage = "Last time updated: " + new Date(cache.getKey()).toString();
                return ipGeolocationResponse;
            }
        }

        Future<HttpResponse<JsonNode>> future = Unirest.get(BASE_URL + ipAddr).asJsonAsync();
        if(future.get().getStatus() != 200) {
            throw new HttpException("ip-api.com returns HTTP_CODE " + future.get().getStatus());
        }
        JSONObject r = future.get().getBody().getObject();

        if(!r.getString("status").equals("success")) {
            throw new HttpException("ip-api.com returns status " + r.getString("status") + " with message \"" + r.getString("message") + "\"");
        }
        IPGeolocation ipGeolocation = new IPGeolocation(
                r.getString("country"),
                r.getString("countryCode"),
                r.getString("region"),
                r.getString("regionName"),
                r.getString("city"),
                r.getString("zip"),
                r.getDouble("lat"),
                r.getDouble("lon"),
                r.getString("timezone"),
                r.getString("isp"),
                r.getString("org"),
                r.getString("as"),
                r.getString("query")
        );

        IPGeolocationResponse ret = new IPGeolocationResponse(ResponseStatus.SUCCESS, ipGeolocation);
        ipGeolocationResponseCachePool.insertCache(ipGeolocation.getIp(), ret);
        return ret;
    }
}
