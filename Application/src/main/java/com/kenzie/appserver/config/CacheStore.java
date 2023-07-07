//package com.kenzie.appserver.config;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import com.kenzie.appserver.service.model.Tool;
//
//import java.util.concurrent.TimeUnit;
//
//public class CacheStore {
//    private Cache<String, Tool> cache;
//
//    public CacheStore(int expiry, TimeUnit timeUnit) {
//        //trying this from the unit Four project to test if itll work here
//        this.cache = CacheBuilder.newBuilder()
//                .expireAfterWrite(expiry, timeUnit)
//                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
//                .build();
//    }
//    public Tool get(String key) {
//        return cache.getIfPresent(key);
//    }
//    public void evict(String key) {
//        cache.invalidate(key);
//    }
//    public void add(String key, Tool value) {
//        cache.put(key, value);
//    }
//}
