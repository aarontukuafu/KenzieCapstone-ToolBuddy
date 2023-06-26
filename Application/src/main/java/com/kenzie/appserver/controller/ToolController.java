package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CreateToolRequest;
import com.kenzie.appserver.controller.model.ToolResponse;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.service.ToolService;
import com.kenzie.appserver.service.model.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private ToolService toolService;

    ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping
    public ResponseEntity<List<ToolResponse>> getAllTools() {
        List<Tool> tools = toolService.getAllTools();
        if (tools == null || tools.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ToolResponse> response = new ArrayList<>();
        for (Tool tool : tools) {
            response.add(this.createToolResponse(tool));
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{toolId}")
    public ResponseEntity<ToolResponse> getToolById(@PathVariable("toolId") String toolId) {
        Tool tool = toolService.findByToolName(toolId);

        if(tool == null) {
            return ResponseEntity.notFound().build();
        }
        ToolResponse toolResponse = createToolResponse(tool);
        return ResponseEntity.ok(toolResponse);
    }
    @PostMapping
    public ResponseEntity<ToolResponse> addNewTool(@RequestBody CreateToolRequest createToolRequest) {
        Tool tool = new Tool(createToolRequest.getId(),
                createToolRequest.getToolName(), createToolRequest.getOwner(),
                createToolRequest.isAvailable(), createToolRequest.getDescription(),
                createToolRequest.getBorrower());
        toolService.addNewTool(tool);

        ToolResponse toolResponse = createToolResponse(tool);

        return ResponseEntity.created(URI.create("/tools/" + toolResponse.getToolId())).body(toolResponse);
    }

    private ToolResponse createToolResponse(Tool tool) {
        ToolResponse toolResponse = new ToolResponse();
        toolResponse.setToolId(tool.getToolId());
        toolResponse.setOwner(tool.getOwner());
        toolResponse.setToolName(tool.getToolName());
        toolResponse.setAvailable(tool.getIsAvailable());
        toolResponse.setDescription(tool.getDescription());
        toolResponse.setComments(tool.getComments());
        toolResponse.setBorrower(tool.getBorrower());
        return toolResponse;
    }
}
