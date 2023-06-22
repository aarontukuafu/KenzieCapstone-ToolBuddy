package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ToolResponse;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.ToolService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/tools"})
public class ToolController {

    private ToolService toolService;
    private UserRecordRepository userRecordRepository;
    private ToolRepository toolRepository;
    private UserResponse userResponse;

    private UserRecord userRecord;
    private ToolRecord toolRecord;
    private UserService userService;

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

   @GetMapping
    public ResponseEntity<List<ToolResponse>> getAllToolsByUserId() {
       Optional<UserRecord> userRecord = userRecordRepository.findById(userResponse.getName());

       if (userRecord.isPresent()) {

           Iterable<Tool> allTools = toolService.getAllToolsByUserId();

           List<ToolResponse> toolResponses = new ArrayList<>();
           for (Tool tool : allTools) {
               ToolResponse toolResponse = createToolResponse(tool);
               toolResponses.add(toolResponse);
           }
           return ResponseEntity.ok(toolResponses);
       }
       return ResponseEntity.ok(new ArrayList<>());
   }
}
