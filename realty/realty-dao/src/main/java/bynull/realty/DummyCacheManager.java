package bynull.realty;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;

/**
 * @author dionis on 10/07/14.
 */
public class DummyCacheManager implements CacheManager {
    @Override
    public Cache getCache(String name) {
        return null;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.emptyList();
    }
}
