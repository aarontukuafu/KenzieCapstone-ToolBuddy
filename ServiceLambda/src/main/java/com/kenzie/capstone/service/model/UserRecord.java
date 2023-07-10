package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "UserDatabase")
public class UserRecord {
    private String name;
    private String username;
    private String password;

    public UserRecord() {
    }
    public UserRecord(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UserRecord(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @DynamoDBAttribute
    public String name() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
