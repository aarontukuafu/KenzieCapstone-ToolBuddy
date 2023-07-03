package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class BorrowToolRequest {

    @NotEmpty
    @JsonProperty("toolId")
    String toolId;

    @NotEmpty
    @JsonProperty("username")
    String username;

    @NotEmpty
    @JsonProperty("password")
    String password;

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
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
