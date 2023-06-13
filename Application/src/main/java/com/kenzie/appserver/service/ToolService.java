package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Tool;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ToolService {
    private ToolRepository toolRepository;

    private UserRecordRepository userRecordRepository;
    private UserResponse userResponse;
    private UserRecord userRecord;
    private UserService userService;
    private ToolRecord toolRecord;
    private User user;

    @Autowired
    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }
    @Autowired
    public ToolService(ToolRepository toolRepository, UserRecordRepository userRecordRepository, UserResponse userResponse, UserRecord userRecord, UserService userService, ToolRecord toolRecord, User user) {
        this.toolRepository = toolRepository;
        this.userRecordRepository = userRecordRepository;
        this.userResponse = userResponse;
        this.userRecord = userRecord;
        this.userService = userService;
        this.toolRecord = toolRecord;
        this.user = user;
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
        tool.setIsAvailable(true);
        tool.setDescription(toolRecord.getDescription());
        tool.setBorrower(toolRecord.getBorrower());
        return tool;
    }

    private ToolRecord createToolRecord(Tool tool) {
        ToolRecord toolRecord = new ToolRecord();
        toolRecord.setToolId(tool.getToolId());
        toolRecord.setOwner(tool.getOwner());
        toolRecord.setToolName(tool.getToolName());
        toolRecord.setIsAvailable(true);
        toolRecord.setDescription(tool.getDescription());
        toolRecord.setBorrower(tool.getBorrower());
        return toolRecord;
    }

    public void addNewTool(Tool tool) {
        if (userService.authenticator(userRecord)) {
            createToolRecord(tool);
        }
    }

    public List<Tool> getAllToolsByUserId() {
        Optional<UserRecord> userRecord1 = userRecordRepository.findById(userRecord.getName());

        if (userRecord1.isPresent()) {
            Optional<ToolRecord> allTools = toolRepository.findById(toolRecord.getOwner());

            List<Tool> tools = new ArrayList<>();
            allTools.ifPresent(toolRecord -> {
                Tool tool = convertToTool(toolRecord);
                tools.add(tool);
            });

            return tools;
        }
        return new ArrayList<>();
    }

}
