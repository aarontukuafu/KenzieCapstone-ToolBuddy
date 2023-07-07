package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class UserCreateRequest  {
    @JsonProperty("name")
    private @NotEmpty String name;
    @JsonProperty("username")
    private @NotEmpty String username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
