package com.kenzie.appserver.controller.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.event.SpringApplicationEvent;

import javax.validation.constraints.NotEmpty;

@JsonInclude
public class UserResponse {
    @JsonProperty("name")
    private @NotEmpty String name;
    @JsonProperty("userName")
    private @NotEmpty String userName;
    @JsonProperty("password")
    private @NotEmpty String password;

    public UserResponse (String name,String userName, String password){
        this.name = name;
        this.userName = userName;
        this.password = password;
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
