package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.User;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, User> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        //trying this from the unit Four project to test if it'll work here
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }
    public User get(String key) {
        return cache.getIfPresent(key);
    }
    public void evict(String key) {
        cache.invalidate(key);
    }
    public void add(String key, User value) {
        cache.put(key, value);
    }
}
