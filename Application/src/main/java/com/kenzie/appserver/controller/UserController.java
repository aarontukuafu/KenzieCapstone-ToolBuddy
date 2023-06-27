package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import java.net.URI;

import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/user"})
public class UserController {
    private UserService userService;
    private UserRecord userRecord;
    private User user;
    private UserRecordRepository userRecordRepository;


    UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/user")
    public ResponseEntity<User> createNewUser(User user) {
        UserRecord record = new UserRecord();
        record.setName(user.getName());
        record.setUserName(user.getUserName());
        record.setPassword(user.getPassword());
        // record.setZipCode(user.getZipCode()); // Implement later per User notes
        userRecordRepository.save(record);
        return ResponseEntity.ok(user);
    }



}
