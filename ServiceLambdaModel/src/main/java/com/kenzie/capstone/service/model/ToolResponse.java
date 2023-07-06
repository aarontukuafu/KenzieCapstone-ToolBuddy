package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude
public class ToolResponse {
    @JsonProperty("toolId")
    private String toolId;

    @JsonProperty("owner")
    private String owner; //GSI //username from UserDB

    @JsonProperty("toolName")
    private String toolName;

    @JsonProperty("isAvailable")
    private boolean isAvailable;

    @JsonProperty("description")
    private String description;

    @JsonProperty("comments")
    private List<String> comments;

    @JsonProperty("borrower")
    private String borrower;

    public ToolResponse(){

    }

    public ToolResponse(String toolId, String owner, String toolName, boolean isAvailable, String description, List<String> comments, String borrower) {
        this.toolId = toolId;
        this.owner = owner;
        this.toolName = toolName;
        this.isAvailable = isAvailable;
        this.description = description;
        this.comments = comments;
        this.borrower = borrower;
    }


    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}
