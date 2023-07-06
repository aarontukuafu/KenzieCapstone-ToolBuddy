package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


import java.util.List;

@DynamoDBTable(tableName = "ToolDatabase")
public class ToolRecord {
    private String toolId;
    private String owner; //GSI //username from UserDB
    private String toolName;
    private boolean isAvailable;
    private String description;
    private List<String> comments;
    private String borrower;

    public ToolRecord() {
    }
    public ToolRecord(String toolId, String owner){
        this.toolId = toolId;
        this.owner = owner;
    }

    public ToolRecord(String toolId, String owner, String toolName, boolean isAvailable, String description, String borrower) {
        this.toolId = toolId;
        this.owner = owner;
        this.toolName = toolName;
        this.isAvailable = isAvailable;
        this.description = description;
        this.borrower = borrower;
    }


    @DynamoDBHashKey(attributeName = "toolId")
    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @DynamoDBAttribute
    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    @DynamoDBAttribute
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }


    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute
    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    @DynamoDBAttribute
    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}
