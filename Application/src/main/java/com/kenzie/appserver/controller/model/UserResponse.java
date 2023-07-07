package com.kenzie.appserver.controller.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.event.SpringApplicationEvent;

import javax.validation.constraints.NotEmpty;

@JsonInclude
public class UserResponse {
    @JsonProperty("name")
    private @NotEmpty String name;
    @JsonProperty("username")
    private @NotEmpty String username;
    @JsonProperty("password")
    private @NotEmpty String password;

    public UserResponse (String name,String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public UserResponse() {

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
