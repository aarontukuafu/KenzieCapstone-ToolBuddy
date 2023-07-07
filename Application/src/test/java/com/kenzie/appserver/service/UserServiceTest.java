package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRecordRepository userRecordRepository;

    @Mock
    private UserResponse userResponse;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);}

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
