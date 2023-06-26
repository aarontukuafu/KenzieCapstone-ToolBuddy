package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private ToolRepository toolRepository;
    private UserRecordRepository userRecordRepository;

    @Autowired
    public UserService(ToolRepository toolRepository, UserRecordRepository userRecordRepository) {
        this.toolRepository = toolRepository;
        this.userRecordRepository = userRecordRepository;
    }
  
    public boolean authenticator(String username, String password) {
        if (userRecordRepository.existsById(username) &&
                userRecordRepository.findById(username).get().getPassword().equals(password)) {
            return true;
//        } else if (username.isEmpty()) {
//            throw new IllegalArgumentException("Username cannot be empty. Please enter username again");
//        } else if (!userResponse.getPassword().equals(userRecord.getPassword())) {
//            throw new IllegalArgumentException("Password does not match. Please re-enter password.");
        }
        return false;
    }

    public User createNewUser(User user) {
        UserRecord record = new UserRecord();
        record.setName(user.getName());
        record.setUserName(user.getUserName());
        record.setPassword(user.getPassword());
       //record.setZipCode(user.getZipCode)); **implement later per User notes
        userRecordRepository.save(record);
        return user;
    }
}
