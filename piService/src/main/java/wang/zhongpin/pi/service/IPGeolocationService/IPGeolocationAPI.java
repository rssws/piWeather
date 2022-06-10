package wang.zhongpin.pi.service.IPGeolocationService;

import org.apache.http.HttpException;
import wang.zhongpin.pi.model.IPGeolocation.IPGeolocationResponse;
import java.util.concurrent.ExecutionException;

public abstract class IPGeolocationAPI {
    public abstract IPGeolocationResponse getIPGeolocation() throws InterruptedException, ExecutionException, HttpException;
    public abstract IPGeolocationResponse getIPGeolocation(String ipAddr) throws ExecutionException, InterruptedException, HttpException;
}
