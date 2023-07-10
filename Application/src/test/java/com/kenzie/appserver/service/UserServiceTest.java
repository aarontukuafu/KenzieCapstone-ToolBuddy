package com.kenzie.appserver.service;

import com.kenzie.appserver.Cache.CachingUserDao;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private CachingUserDao cache;

    @Mock
    private UserRecordRepository userRecordRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validCredentials_CanFindUser(){
        //GIVEN
        String username = "testuser";
        String password = "testpassword";
        UserRecord userRecord = new UserRecord();
        userRecord.setUsername(username);
        userRecord.setPassword(password);

        when(cache.findById(username)).thenReturn(Optional.empty());
        when(userRecordRepository.findById(username)).thenReturn(Optional.of(userRecord));

        //WHEN
        boolean findingUser = userService.authenticator(username, password);

        //THEN
        assertTrue(findingUser, "User Found!");
        verify(cache).addToCache(userRecord);
    }

    @Test
    public void invalidCredentials_CannotFindUser(){
        //GIVEN
        String username = "testuser";
        String password = "testpassword";
        UserRecord userRecord = new UserRecord("John Doe", username, "wrong password");

        when(cache.findById(username)).thenReturn(Optional.of(userRecord));
        when(userRecordRepository.existsById(username)).thenReturn(false);

        //WHEN
        boolean findingUser = userService.authenticator(username, password);

        //THEN
        assertFalse(findingUser, "User Not Found!");
    }

    @Test
    public void testAuthenticator_UserNotInCache(){
        //GIVEN
        String username = "testuser";
        String password = "testpassword";
        UserRecord userRecord = new UserRecord("testuser", username, password);

        when(cache.findById(username)).thenReturn(Optional.empty());
        when(userRecordRepository.findById(username)).thenReturn(Optional.of(userRecord));

        //WHEN
        boolean findUser = userService.authenticator(username,password);

        //THEN
        assertTrue(findUser);
        verify(userRecordRepository, times(1)).findById(username);
        verify(cache, times(1)).addToCache(userRecord);
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
        assertNotNull(userResponse, "The UserResponse should not be null");
        Assertions.assertEquals(userCreateRequest.getName(), userResponse.getName(), "The name should match");
        Assertions.assertEquals(userCreateRequest.getUsername(), userResponse.getUsername(), "The username should match");
        Assertions.assertEquals(userCreateRequest.getPassword(), userResponse.getPassword(), "The password should match");
    }

    @Test
    public void userNotFound_ReturnFalse(){
        //GIVEN
        String username = "John";
        String password = "password";

        when(cache.findById(username)).thenReturn(Optional.empty());
        when(userRecordRepository.findById(username)).thenReturn(Optional.empty());

        //WHEN
        boolean result = userService.authenticator(username, password);

        //THEN
        assertFalse(result);
    }

    @Test
    public void getUser_UserExistsInCache(){
        //GIVEN
        String username = "fakeuser";
        UserRecord userRecord = new UserRecord("fakeuser", username, "password");

        when(cache.findById(username)).thenReturn(Optional.of(userRecord));

        //WHEN
        UserResponse userResponse = userService.getUser(username);

        //THEN
        Assertions.assertNotNull(userResponse);
        Assertions.assertEquals(userRecord.getName(), userResponse.getName());
        Assertions.assertEquals(userRecord.getUsername(), userResponse.getUsername());
        Assertions.assertEquals(userRecord.getPassword(), userResponse.getPassword());
    }

    @Test
    public void getUser_UserNotInCache(){
        //GIVEN
        String username = "John";

        when(cache.findById(username)).thenReturn(Optional.empty());

        //WHEN
        UserResponse userResponse = userService.getUser(username);

        //THEN
        assertNull(userResponse);
    }

    @Test
    public void deleteUser_ReturnsTrue(){
        //GIVEN
        String username = "fakeuser";

        //WHEN
        userService.deleteUser(username);

        //THEN
        verify(userRecordRepository,times(1)).deleteById(username);
        verify(cache, times(1)).invalidate(username);
    }

}
