package wang.zhongpin.pi.model;

import java.util.Map;

public class RequestLimitation {
    private CachePool<Integer> requestCountsPerIP;
    private int limitation;

    public RequestLimitation(int timePeriod, int limitation) {
        this.requestCountsPerIP = new CachePool<>(timePeriod);
        this.limitation = limitation;
    }

    public boolean isTooFrequent(String remoteAddr) {
        // remove the old cache first before doing anything
        this.requestCountsPerIP.removeOldCache();

        if (this.requestCountsPerIP.getCache(remoteAddr) == null) {
            this.requestCountsPerIP.insertCache(remoteAddr, 1);
            return false;
        } else {
            // ip exists in pool
            Map.Entry<Long, Integer> cache = this.requestCountsPerIP.getCache(remoteAddr);
            int requestCounter = cache.getValue();
            if (requestCounter + 1 <= this.limitation) {
                // not exceed the limit
                requestCountsPerIP.insertCache(remoteAddr, requestCounter + 1);
                return false;
            } else {
                // exceed the limit
                // renew the timestamp, without adding up to prevent overflow
                requestCountsPerIP.insertCache(remoteAddr, requestCounter);
                // reject the request
                return true;
            }
        }
    }
}
