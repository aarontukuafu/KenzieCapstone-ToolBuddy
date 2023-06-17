package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.service.model.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolService {
    private ToolRepository toolRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
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
    public Tool addNewTool (Tool tool) {

        ToolRecord toolRecord = new ToolRecord();

        toolRecord.setToolId(tool.getToolId());
        toolRecord.setOwner(tool.getOwner());
        toolRecord.setToolName(tool.getToolName());
        toolRecord.setIsAvailable(tool.getIsAvailable());
        toolRecord.setDescription(tool.getDescription());
        toolRecord.setBorrower(tool.getBorrower());

        toolRepository.save(toolRecord);

        return tool;
    }

}
