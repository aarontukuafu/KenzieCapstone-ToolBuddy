package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateToolRequest {
    @JsonProperty("toolName")
    private String toolName;

    @JsonProperty("id")
    private int id;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("isAvailable")
    private boolean isAvailable;

    @JsonProperty("description")
    private String description;

    @JsonProperty("comments")
    private List<String> comments;

    @JsonProperty("borrower")
    private String borrower;

    public String getToolName() {
        return toolName;
    }
    public int getId() {
        return id;
    }
    public String getOwner() {
        return owner;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getComments() {
        return comments;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}
