package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Objects;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.springframework.data.annotation.Id;

@DynamoDBTable(
        tableName = "UserDatabase"
)
public class UserRecord {
    private String name;
    private String username;
    private String password;

    public UserRecord(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public UserRecord() {

    }


    @Id
    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {return this.name;}

    public void setName(String userId) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
