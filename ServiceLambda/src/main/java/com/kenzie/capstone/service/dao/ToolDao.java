package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.Tool;
import com.kenzie.capstone.service.model.ToolRecord;

import java.util.List;

public class ToolDao {

    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public ToolDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public List<ToolRecord> getAllTools() {
        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        PaginatedScanList<ToolRecord> toolRecords = mapper.scan(ToolRecord.class, scan);
        return toolRecords.subList(0, toolRecords.size());
    }

    public List<ToolRecord> getAllToolsByOwnerId(String ownerId) {
        ToolRecord toolRecord = new ToolRecord();
        toolRecord.setOwner(ownerId);

        DynamoDBQueryExpression<ToolRecord> queryExpression = new DynamoDBQueryExpression<ToolRecord>()
                .withIndexName("owner")
                .withHashKeyValues(toolRecord)
                .withConsistentRead(false);

        return mapper.query(ToolRecord.class, queryExpression);
    }

    public ToolRecord getToolById(String toolId) {
        ToolRecord toolRecord = mapper.load(ToolRecord.class, toolId);

        if (toolRecord == null) {
            throw new IllegalArgumentException("Tool not found");
        }

        return toolRecord;
    }

    public ToolRecord addNewTool(ToolRecord toolRecord) {
        try {
            mapper.save(toolRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "ToolId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return toolRecord;
    }

    public ToolRecord borrowTool(String toolId, String borrower) {
        ToolRecord toolRecord = mapper.load(ToolRecord.class, toolId);

        if (toolRecord == null) {
            throw new IllegalArgumentException("Tool not found");
        }

        toolRecord.setBorrower(borrower);
        toolRecord.setIsAvailable(false);
        mapper.save(toolRecord);
        return toolRecord;
    }

    public void removeTool(String toolId) {
        ToolRecord toolRecord = mapper.load(ToolRecord.class, toolId);

        if (toolRecord == null) {
            throw new IllegalArgumentException("Tool not found");
        }

        mapper.delete(toolRecord);
    }

//    public ExampleRecord setExampleData(String id, String data) {
//        ExampleRecord exampleRecord = new ExampleRecord();
//        exampleRecord.setId(id);
//        exampleRecord.setData(data);
//
//        try {
//            mapper.save(exampleRecord, new DynamoDBSaveExpression()
//                    .withExpected(ImmutableMap.of(
//                            "id",
//                            new ExpectedAttributeValue().withExists(false)
//                    )));
//        } catch (ConditionalCheckFailedException e) {
//            throw new IllegalArgumentException("id already exists");
//        }
//
//        return exampleRecord;
//    }


}
