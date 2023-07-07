package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ToolConverter {
    
    
    public static ToolRecord fromRequestToRecord(CreateToolRequest toolRequest) {
        ToolRecord toolRecord = new ToolRecord();
        toolRecord.setToolId(UUID.randomUUID().toString());
        toolRecord.setOwner(toolRequest.getOwner());
        toolRecord.setToolName(toolRequest.getToolName());
        toolRecord.setIsAvailable(true);
        toolRecord.setDescription(toolRequest.getDescription());
        toolRecord.setBorrower(toolRequest.getBorrower());
        return toolRecord;
    }

    public static ToolResponse fromRecordToResponse(ToolRecord toolRecord) {
        ToolResponse toolResponse = new ToolResponse();
        toolResponse.setToolId(toolRecord.getToolId());
        toolResponse.setOwner(toolRecord.getOwner());
        toolResponse.setToolName(toolRecord.getToolName());
        toolResponse.setIsAvailable(toolRecord.getIsAvailable());
        toolResponse.setDescription(toolRecord.getDescription());
        toolResponse.setBorrower(toolRecord.getBorrower());
        return toolResponse;
    }

    public static CreateToolRequest fromToolToRequest(Tool tool) {
        CreateToolRequest createToolRequest = new CreateToolRequest();
        createToolRequest.setToolId(tool.getToolId());
        createToolRequest.setOwner(tool.getOwner());
        createToolRequest.setToolName(tool.getToolName());
        createToolRequest.setIsAvailable(tool.getIsAvailable());
        createToolRequest.setDescription(tool.getDescription());
        createToolRequest.setComments(tool.getComments());
        createToolRequest.setBorrower(tool.getBorrower());
        return createToolRequest;
    }

    public static ToolRecordResponse fromRequestToToolRecord(List<ToolRecord> toolRecordList, CreateToolRecordRequest createToolRecordRequest) {
        ToolRecordResponse toolRecordResponse = new ToolRecordResponse();
        toolRecordResponse.setToolId(createToolRecordRequest.g);
        toolRecordResponse.setOwner(createToolRecordRequest.getOwner());
        toolRecordResponse.setToolName(createToolRecordRequest.getToolName());
        toolRecordResponse.setIsAvailable(createToolRecordRequest.getIsAvailable());
        toolRecordResponse.setDescription(createToolRecordRequest.getDescription());
        toolRecordResponse.setBorrower(createToolRecordRequest.getBorrower());
        toolRecordResponse.add(toolRecordResponse);
        return toolRecordResponse;
    }
}
