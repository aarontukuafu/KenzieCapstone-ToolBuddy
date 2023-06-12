package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class UserCreateRequest  {
    @JsonProperty("name")
    private @NotEmpty String name;
    @JsonProperty("userName")
    private @NotEmpty String userName;
    @JsonProperty("password")
    private @NotEmpty String password;

    public UserCreateRequest(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
