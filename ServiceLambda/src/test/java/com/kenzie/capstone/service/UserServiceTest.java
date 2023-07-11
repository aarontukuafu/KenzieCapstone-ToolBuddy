package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.UserDao;
import com.kenzie.capstone.service.model.UserRecord;
import com.kenzie.capstone.service.util.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class UserServiceTest {
    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userDao);
    }

    @Test
    public void testGetUser() {
        //GIVEN
        List<UserRecord> expectedUserRecords = Arrays.asList(
                new UserRecord("John", "Doe"),
                new UserRecord("Jane", "Smith")
        );
        Mockito.when(userDao.getUser()).thenReturn(expectedUserRecords);

        //WHEN
        List<UserRecord> actualUserRecords = userService.getUser();

        //THEN
        Mockito.verify(userDao).getUser();
        Assertions.assertEquals(expectedUserRecords, actualUserRecords);
    }
}
