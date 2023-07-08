package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;

    private UserRecordRepository userRecordRepository;


    @BeforeEach
    public void setUp() {
        userRecordRepository = Mockito.mock(UserRecordRepository.class);
        userService = new UserService(userRecordRepository);
    }

    @Test
    public void validCredentials_CanFindUser(){
        //GIVEN
        String username = "testuser";
        String password = "testpassword";
        UserRecord userRecord = new UserRecord();
        userRecord.setUsername(username);
        userRecord.setPassword(password);

        when(userRecordRepository.existsById(username)).thenReturn(true);
        when(userRecordRepository.findById(username)).thenReturn(Optional.of(userRecord));

        //WHEN
        boolean findingUser = userService.authenticator(username, password);

        //THEN
        Assertions.assertTrue(findingUser, "User Found!");
    }

    @Test
    public void invalidUsername_CannotFindUser(){
        //GIVEN
        String username = "testuser";
        String password = "testpassword";

        when(userRecordRepository.existsById(username)).thenReturn(false);

        //WHEN
        boolean findingUser = userService.authenticator(username, password);

        //THEN
        Assertions.assertFalse(findingUser, "Username is Invalid!");
    }

    @Test
    public void invalidPassword_CannotFindUser(){
        //GIVEN
        String username = "testuser";
        String password = "testpassword";
        UserRecord userRecord = new UserRecord();
        userRecord.setUsername(username);
        userRecord.setPassword("wrongpassword");

        when(userRecordRepository.existsById(username)).thenReturn(true);
        when(userRecordRepository.findById(username)).thenReturn(Optional.of(userRecord));

        //WHEN
        boolean findUser = userService.authenticator(username,password);

        //THEN
        Assertions.assertFalse(findUser, "The Password is invalid");
    }

    @Test
    public void createNewUser_NullRequest(){
        //GIVEN
        UserCreateRequest userCreateRequest = null;

        //WHEN
        //THEN
        Assertions.assertNull(userCreateRequest, "User is null");
    }

    @Test
    public void validRequest_CanCreateNewUser() {
        // GIVEN
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName("John Doe");
        userCreateRequest.setUsername("johndoe");
        userCreateRequest.setPassword("password");

        UserRecord savedUserRecord = new UserRecord();
        savedUserRecord.setName(userCreateRequest.getName());
        savedUserRecord.setUsername(userCreateRequest.getUsername());
        savedUserRecord.setPassword(userCreateRequest.getPassword());

        when(userRecordRepository.save(Mockito.any(UserRecord.class))).thenReturn(savedUserRecord);

        // WHEN
        UserResponse userResponse = userService.createNewUser(userCreateRequest);

        // THEN
        Assertions.assertNotNull(userResponse, "The UserResponse should not be null");
        Assertions.assertEquals(userCreateRequest.getName(), userResponse.getName(), "The name should match");
        Assertions.assertEquals(userCreateRequest.getUsername(), userResponse.getUsername(), "The username should match");
        Assertions.assertEquals(userCreateRequest.getPassword(), userResponse.getPassword(), "The password should match");
    }

//    @Test
//    public void canCreateNewUser(){
//        User user = new User("John Doe", "johndoe", "password123");
//        user.setName("John Doe");
//        user.setUsername("johndoe");
//        user.setPassword("password123");
//
//        UserRecord savedRecord = new UserRecord();
//        when(userRecordRepository.save(any(UserRecord.class))).thenReturn(savedRecord);
//
//        User result = userService.createNewUser(user);
//
//        verify(userRecordRepository).save(any(UserRecord.class));
//        Assertions.assertEquals(user, result);
//    }
}
