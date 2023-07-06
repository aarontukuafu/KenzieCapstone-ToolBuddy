package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.CreateToolRequest;
import com.kenzie.capstone.service.model.ToolRecord;
import com.kenzie.capstone.service.model.ToolResponse;

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
}
