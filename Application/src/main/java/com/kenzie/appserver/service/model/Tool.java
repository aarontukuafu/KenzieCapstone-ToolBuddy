package com.kenzie.appserver.service.model;

import java.util.List;

public class Tool {
    private int toolId;
    private String owner; //GSI //username from UserDB
    private String toolName;
    private boolean isAvailable;
    private String description;
    private List<String> comments;
    private String borrower;

    public Tool(){}

    public Tool(int toolId, String owner, String toolName, boolean isAvailable, String description, String borrower) {
        this.toolId = toolId;
        this.owner = owner;
        this.toolName = toolName;
        this.isAvailable = isAvailable;
        this.description = description;
        this.borrower = borrower;
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
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

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
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
