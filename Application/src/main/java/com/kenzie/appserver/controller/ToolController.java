package com.kenzie.appserver.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.kenzie.appserver.controller.model.BorrowToolRequest;
//import com.kenzie.appserver.controller.model.CreateToolRequest;
//import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.controller.model.UserCreateToolRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.UserRecordRepository;
//import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.UserService;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ToolRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kenzie.capstone.service.model.Tool;
import com.kenzie.capstone.service.model.ToolResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private final LambdaServiceClient toolService;
    private UserRecordRepository userRecordRepository;
//    private ToolRepository toolRepository;

    private UserRecord userRecord;
    //    private ToolRecord toolRecord;
    private UserService userService;

    ToolController(LambdaServiceClient toolService, UserService userService) {
        this.toolService = toolService;
        this.userService = userService;
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

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ToolResponse>> getAllToolsByOwnerId(@PathVariable("ownerId") String ownerId) {
        UserResponse userRecord = userService.getUser(ownerId);

        if (userRecord != null) {

            List<Tool> allTools = toolService.getAllToolsByOwnerId(ownerId);

            List<ToolResponse> toolResponses = new ArrayList<>();
            for (Tool tool : allTools) {
                ToolResponse toolResponse = createToolResponse(tool);
                toolResponses.add(toolResponse);
            }
            return ResponseEntity.ok(toolResponses);
        }
        return ResponseEntity.badRequest().build(); //Use frontend to display message to User
    }

    @GetMapping("/tool/{toolId}")
    public ResponseEntity<ToolResponse> getToolById(@PathVariable String toolId) {
        Tool tool = toolService.getToolById(toolId);
        if (tool == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createToolResponse(tool));
    }

    @PostMapping("/tools")
    public ResponseEntity<ToolResponse> addNewTool(@RequestBody UserCreateToolRequest userCreateToolRequest) {
        if (userService.authenticator(userCreateToolRequest.getUsername(), userCreateToolRequest.getPassword())) {
            Tool createdTool = toolService.addNewTool(new Tool(UUID.randomUUID().toString(),
                    userCreateToolRequest.getUsername(),
                    userCreateToolRequest.getToolName(),
                    true,
                    userCreateToolRequest.getDescription(),
                    null));
            return ResponseEntity.ok().body(this.createToolResponse(createdTool));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/borrowTool") //Switched to Put //Need Return Method
    public ResponseEntity<ToolResponse> borrowTool(@RequestBody BorrowToolRequest borrowToolRequest) {
        if (userService.authenticator(borrowToolRequest.getUsername(), borrowToolRequest.getPassword())) {
            //user is valid
            //What tool do the want to borrow? hint hint getToolById
            Tool tool = toolService.getToolById(borrowToolRequest.getToolId());

            //if tool is not Null && isAvailable is True THEN call borrow method with correct user and correct tool
            if (tool != null && tool.getIsAvailable()) {
                tool = toolService.borrowTool(borrowToolRequest.getToolId(), borrowToolRequest.getUsername());
                return ResponseEntity.ok(createToolResponse(tool));
            } else {
                // The tool is either not found or not available.
                return ResponseEntity.badRequest().build();
            }
        } else {
            // Invalid user credentials
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{toolId}")
    public ResponseEntity<ToolResponse> removeTool(@PathVariable("toolId") String toolId, @RequestParam ("username") String
            username, @RequestParam ("password") String password) {
        if (userService.authenticator(username, password)) {

            Tool tool = toolService.getToolById(toolId);
            toolService.deleteTool(toolId);
            return ResponseEntity.ok(createToolResponse(tool));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private ToolResponse createToolResponse(Tool tool) {
        ToolResponse toolResponse = new ToolResponse();
        toolResponse.setToolId(tool.getToolId());
        toolResponse.setOwner(tool.getOwner());
        toolResponse.setToolName(tool.getToolName());
        toolResponse.setIsAvailable(tool.getIsAvailable());
        toolResponse.setDescription(tool.getDescription());
//        toolResponse.setComments(tool.getComments());
        toolResponse.setBorrower(tool.getBorrower());
        return toolResponse;
    }
}

