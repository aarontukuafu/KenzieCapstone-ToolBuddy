package com.kenzie.appserver.Cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CachingUserDao {
    private final LoadingCache<String, Optional<UserRecord>> userCache;

    @Autowired
    public CachingUserDao(UserRecordRepository userRepository) {
        userCache = CacheBuilder.newBuilder()
                .build(CacheLoader.from(userRepository::findById));
    }

    public Optional<UserRecord> findById(String username) {
        return userCache.getUnchecked(username);
    }

    public void invalidate(String username){
        userCache.invalidate(username);
    }

    public void addToCache(UserRecord userRecord) {
        userCache.put(userRecord.getUsername(), Optional.of(userRecord));
    }
}
