package com.kenzie.appserver.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.kenzie.appserver.controller.model.BorrowToolRequest;
import com.kenzie.appserver.controller.model.CreateToolRequest;
import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.UserService;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kenzie.capstone.service.model.Tool;
import com.kenzie.capstone.service.model.ToolResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private LambdaServiceClient toolService;
    private UserRecordRepository userRecordRepository;
    private ToolRepository toolRepository;

    private UserRecord userRecord;
    private ToolRecord toolRecord;
    private UserService userService;

    ToolController(LambdaServiceClient toolService) {
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

   @GetMapping("/{owner}")
    public ResponseEntity<List<ToolResponse>> getAllToolsByOwnerId(@PathVariable String owner) {
       Optional<UserRecord> userRecord = userRecordRepository.findById(owner);

       if (userRecord.isPresent()) {

           List<Tool> allTools = toolService.getAllToolsByOwnerId(owner);

           List<ToolResponse> toolResponses = new ArrayList<>();
           for (Tool tool : allTools) {
               ToolResponse toolResponse = createToolResponse(tool);
               toolResponses.add(toolResponse);
           }
           return ResponseEntity.ok(toolResponses);
       }
       return ResponseEntity.badRequest().build(); //Use frontend to display message to User
   }

   @PostMapping("/tools")
   public ResponseEntity<ToolResponse> addNewTool(@RequestBody Tool tool, @RequestParam String username, @RequestParam String password) {
       if (userService.authenticator(username, password)) {
           Tool createdTool = toolService.addNewTool(tool);
           return ResponseEntity.ok().body(this.createToolResponse(createdTool));
       } else {
           return ResponseEntity.badRequest().build();
       }
   }

//    @PutMapping("/borrowTool") //Switched to Put
//    public ResponseEntity<ToolResponse> borrowTool(@RequestBody BorrowToolRequest borrowToolRequest) {
//        Tool tool = toolService.borrowTool(borrowToolRequest.getToolId(), borrowToolRequest.getUsername(), borrowToolRequest.getPassword());
//        if ( tool == null){
//            return ResponseEntity.badRequest().build();
//        } else return ResponseEntity.ok(createToolResponse(tool));
//    }

    @PutMapping("/{toolId}/borrow")
    public ResponseEntity<ToolResponse> borrowTool(@PathVariable String toolId, @RequestParam String borrower) {
        Tool borrowedTool = toolService.borrowTool(toolId, borrower);
        return ResponseEntity.ok().body(this.createToolResponse(borrowedTool));
    }


    @DeleteMapping("/toolId")
    public ResponseEntity<Void> removeTool(@PathVariable String toolId, @RequestParam String username, @RequestParam String password) {
        if (userService.authenticator(username, password)) {
            toolService.deleteTool(toolId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
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
