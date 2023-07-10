package com.kenzie.capstone.service.util;

import com.kenzie.capstone.service.ToolService;
import com.kenzie.capstone.service.converter.ToolConverter;
import com.kenzie.capstone.service.dao.ToolDao;

import com.kenzie.capstone.service.dao.UserDao;
import com.kenzie.capstone.service.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserDao userDao;

    static final Logger log = LogManager.getLogger();

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public List<UserRecord> getUser() {
        return userDao.getUser();
    }
}
