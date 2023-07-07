package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.catalina.LifecycleState;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UserCreateToolRequest {

    @NotEmpty
    @JsonProperty("toolName")
    private String toolName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    public String getToolName() {
        return toolName;
    }

    public String getDescription() {
        return description;
    }



    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void setDescription(String description) {
        this.description = description;
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
