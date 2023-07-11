package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

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

    @JsonProperty("borrower")
    private String borrower;

    public ToolResponse(){

    }

    public ToolResponse(String toolId, String owner, String toolName, boolean isAvailable, String description, String borrower) {
        this.toolId = toolId;
        this.owner = owner;
        this.toolName = toolName;
        this.isAvailable = isAvailable;
        this.description = description;
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

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolResponse that = (ToolResponse) o;
        return isAvailable == that.isAvailable &&
                Objects.equals(toolId, that.toolId) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(toolName, that.toolName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(borrower, that.borrower);
    }
    @Override
    public int hashCode() {
        return Objects.hash(toolId, owner, toolName, isAvailable, description, borrower);
    }
}
