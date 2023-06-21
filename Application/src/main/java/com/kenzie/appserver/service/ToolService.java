package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.service.model.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kenzie.appserver.config.CacheStore;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolService {
    private ToolRepository toolRepository;
    private CacheStore cache;

    @Autowired
    public ToolService(ToolRepository toolRepository, CacheStore cache) {
        this.toolRepository = toolRepository;
        this.cache = cache;
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
    //Testing to see if this will pull what i need, grabbed from Unit Four project
    public Tool findByToolName(String toolName) {
        Tool cachedTool = cache.get(toolName);

        if(cachedTool != null) {
            return cachedTool;
        }
        Tool toolFromBackendService = toolRepository
                .findById(toolName)
                .map(tool -> new Tool(tool.getToolId(),
                        tool.getOwner(),
                        tool.getToolName(),
                        tool.getIsAvailable(),
                        tool.getDescription(),
                        tool.getBorrower()))
                .orElse(null);
        if(toolFromBackendService != null) {
            cache.add(toolFromBackendService.getToolName(), toolFromBackendService);
        }
        return toolFromBackendService;
    }
}
