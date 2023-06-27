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
    private String userName;
    private String password;

    public UserRecord(String name, String userName, String password){
        this.name = name;
        this.userName = userName;
        this.password = password;
    }
    public UserRecord() {

    }


    @DynamoDBAttribute(attributeName = "name")
    public String getName() {return this.name;}

    public void setName(String userId) {
        this.name = name;
    }
    @Id
    @DynamoDBHashKey(attributeName = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
