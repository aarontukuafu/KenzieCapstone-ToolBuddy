package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.service.model.Tool;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolService {
    private ToolRepository toolRepository;

    private UserRecordRepository userRecordRepository;
    private UserResponse userResponse;
    private UserService userService;

    private User user;

    @Autowired
    public ToolService(ToolRepository toolRepository, UserService userService) {
        this.toolRepository = toolRepository;
        this.userService = userService;
    }

    public List<Tool> getAllTools() {
        Iterable<ToolRecord> allTools = toolRepository.findAll();

        List<Tool> tools = new ArrayList<>();
        for (ToolRecord toolRecord : allTools) {
            Tool tool = convertToTool(toolRecord);
            tools.add(tool);
        }
        return tools;
    }

    private Tool convertToTool(ToolRecord toolRecord) {
        Tool tool = new Tool();
        tool.setToolId(toolRecord.getToolId());
        tool.setOwner(toolRecord.getOwner());
        tool.setToolName(toolRecord.getToolName());
        tool.setIsAvailable(toolRecord.getIsAvailable());
        tool.setDescription(toolRecord.getDescription());
        tool.setBorrower(toolRecord.getBorrower());
        return tool;
    }

    private ToolRecord convertToToolRecord(Tool tool) {
        ToolRecord toolRecord = new ToolRecord();
        toolRecord.setToolId(tool.getToolId());
        toolRecord.setOwner(tool.getOwner());
        toolRecord.setToolName(tool.getToolName());
        toolRecord.setIsAvailable(toolRecord.getIsAvailable());
        toolRecord.setDescription(tool.getDescription());
        toolRecord.setBorrower(tool.getBorrower());
        return toolRecord;
    }

    public void addNewTool(Tool tool, String username, String password) {
        if (userService.authenticator(username, password)) {
            convertToToolRecord(tool);
        }
    }

    public List<Tool> getAllToolsByOwnerId(String owner) {
        List<ToolRecord> toolRecords = toolRepository.findByOwner(owner);
        if (toolRecords == null) {
            return new ArrayList<>();
        }

            List<Tool> tools = new ArrayList<>();
            for (ToolRecord toolRecord : toolRecords) {
                Tool tool = convertToTool(toolRecord);
                tools.add(tool);
            }

        return tools;
    }

    public void removeTool(Tool tool, String username, String password) {
        if (userService.authenticator(username, password)) {
            ToolRecord toolRecord = convertToToolRecord(tool);
            toolRepository.delete(toolRecord);
        }
    }

    public Tool borrowTool(String toolId, String borrower, String password){
        if (toolRepository.existsById(toolId) && userService.authenticator(borrower, password)){
            ToolRecord toolRecord = toolRepository.findById(toolId).get();
                if (toolRecord.getIsAvailable()){
                    toolRecord.setIsAvailable(false);
                    toolRecord.setBorrower(borrower);
                    toolRepository.save(toolRecord);
                    return convertToTool(toolRecord);
                }
        }
        return null;
    }
}
