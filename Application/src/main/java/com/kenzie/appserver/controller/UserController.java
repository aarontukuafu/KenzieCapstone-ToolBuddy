package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import java.net.URI;

import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserRecord userRecord;
    private User user;
    private UserRecordRepository userRecordRepository;


    UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping//check if User already exists in DB
    public ResponseEntity<UserResponse> createNewUser(@RequestBody UserCreateRequest userCreateRequest) {

        if (userCreateRequest.getName() == null || userCreateRequest.getName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Customer Name");
        }
        if (userCreateRequest.getUsername() == null || userCreateRequest.getUsername().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Username");
        }
        if (userCreateRequest.getPassword() == null || userCreateRequest.getPassword().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Password");
        }

        UserResponse userResponse = userService.createNewUser(userCreateRequest);

        return ResponseEntity.created(URI.create("/user" + userResponse.getUsername())).body(userResponse);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username, @RequestParam String authUsername, @RequestParam String authPassword) {
        if (!userService.authenticator(authUsername, authPassword) || !username.equals(authUsername)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request");
        }
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
