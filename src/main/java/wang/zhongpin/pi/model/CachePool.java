package wang.zhongpin.pi.model;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CachePool <T> {
    /**
     * Cache pool structure: <cityName, <timestamp of the last time updated, cache content>>
     */
    private Map<String, Map.Entry<Long, T>> cachePool = new HashMap<>();
    private int lifecycle;

    /**
     * Constructor. Initialize the lifecycle of caches.
     * @param lifecycle The valid duration (ms) of caches. Out-dated cache will be removed.
     */
    public CachePool(int lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Map.Entry<Long, T> getCache(String key) {
        if (cachePool.containsKey(key)) {
            Map.Entry<Long, T> cache = cachePool.get(key);
            if (isValid(cache)) {
                return cache;
            }
        }
        return null;
    }

    private boolean isValid(Map.Entry<Long, T> cache) {
        return System.currentTimeMillis() - cache.getKey() <= lifecycle;
    }

    /**
     * Remove all out-dated caches in the pool.
     */
    public void removeOldCache() {
        cachePool.entrySet().removeIf(entry -> !isValid(entry.getValue()));
    }

    public void insertCache(String key, T content) {
        cachePool.put(key, new AbstractMap.SimpleImmutableEntry<>(System.currentTimeMillis(), content));
        removeOldCache();
    }
}
