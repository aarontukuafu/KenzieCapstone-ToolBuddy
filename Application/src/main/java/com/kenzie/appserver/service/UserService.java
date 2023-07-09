package com.kenzie.appserver.service;

import com.kenzie.appserver.Cache.CachingUserDao;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
//import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.UserRecordRepository;
//import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;


@Service
public class UserService {

    private UserRecordRepository userRecordRepository;
    private CachingUserDao cache;

    @Autowired
    public UserService(UserRecordRepository userRecordRepository, CachingUserDao cache) {
        this.userRecordRepository = userRecordRepository;
        this.cache = cache;
    }
  
    public boolean authenticator(String username, String password) {
        Optional<UserRecord> userRecord = cache.findById(username);

        if (!userRecord.isPresent()) {
            userRecord = userRecordRepository.findById(username);
            if (userRecord.isPresent()) {
                cache.addToCache(userRecord.get());  // Add the user to the cache
            }
        }

        if (userRecord.isPresent() && userRecord.get().getPassword().equals(password)) {
            return true;
        }
        return false;
//        if (cache.findById(username).isPresent() &&
//                cache.findById(username).get().getPassword().equals(password)) {
//            return true;
//        } else if (username.isEmpty()) {
//            throw new IllegalArgumentException("Username cannot be empty. Please enter username again");
//        } else if (!userResponse.getPassword().equals(userRecord.getPassword())) {
//            throw new IllegalArgumentException("Password does not match. Please re-enter password.");
//    }
//        return false;
    }

//    public User createNewUser(User user) {
//        UserRecord record = new UserRecord();
//        record.setName(user.getName());
//        record.setUsername(user.getUsername());
//        record.setPassword(user.getPassword());
//        userRecordRepository.save(record);
//        return user;
//    }

    public UserResponse createNewUser(UserCreateRequest userCreateRequest) {
        UserRecord userRecord = toUserRecord(userCreateRequest);

        userRecordRepository.save(userRecord);
        cache.addToCache(userRecord);

        return toUserResponseFromRecord(userRecord);
    }

    public UserResponse getUser(String username) {
        Optional<UserRecord> userRecord = cache.findById(username);

        return userRecord.map(this::toUserResponseFromRecord).orElse(null);
    }

    public void deleteUser(String username) {
        userRecordRepository.deleteById(username);
        cache.invalidate(username); // Remove the user from the cache
    }

    private UserRecord toUserRecord(UserCreateRequest userCreateRequest) {
        UserRecord userRecord = new UserRecord();
        userRecord.setName(userCreateRequest.getName());
        userRecord.setUsername(userCreateRequest.getUsername());
        userRecord.setPassword(userCreateRequest.getPassword());

        return userRecord;
    }

    private UserResponse toUserResponseFromRecord(UserRecord userRecord) {
        if (userRecord == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setName(userResponse.getName());
        userResponse.setUsername(userRecord.getUsername());
        userResponse.setPassword(userRecord.getPassword());

        return userResponse;
    }


}
