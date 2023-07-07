package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.ToolConverter;
import com.kenzie.capstone.service.dao.ToolDao;

import com.kenzie.capstone.service.model.CreateToolRequest;
import com.kenzie.capstone.service.model.Tool;
import com.kenzie.capstone.service.model.ToolRecord;
import com.kenzie.capstone.service.model.ToolResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class ToolService {
    private ToolDao toolDao;


    static final Logger log = LogManager.getLogger();

    @Inject
    public ToolService(ToolDao toolDao) {
        this.toolDao = toolDao;
    }

    public List<ToolResponse> getAllTools() {
        return toolDao.getAllTools();
    }

    public List<ToolRecord> getAllToolsByOwnerId(String owner) {
        return toolDao.getAllToolsByOwnerId(owner);
    }

    public ToolResponse getToolById(String toolId) {
        ToolRecord toolRecord = toolDao.getToolById(toolId);
        return ToolConverter.fromRecordToResponse(toolRecord);
    }

    public ToolResponse addNewTool(CreateToolRequest toolRequest) {
        ToolRecord toolRecord = ToolConverter.fromRequestToRecord(toolRequest);
        toolDao.addNewTool(toolRecord);
        return ToolConverter.fromRecordToResponse(toolRecord);
    }

    public ToolRecord borrowTool(String toolId, String borrower) {
        return toolDao.borrowTool(toolId, borrower);
    }

    public void removeTool(String toolId) {
        toolDao.removeTool(toolId);
    }


//    private ToolRecord convertToToolRecord(Tool tool) {
//        ToolRecord toolRecord = new ToolRecord();
//        toolRecord.setToolId(tool.getToolId());
//        toolRecord.setOwner(tool.getOwner());
//        toolRecord.setToolName(tool.getToolName());
//        toolRecord.setIsAvailable(toolRecord.getIsAvailable());
//        toolRecord.setDescription(tool.getDescription());
//        toolRecord.setBorrower(tool.getBorrower());
//        return toolRecord;
//    }
//
//    private Tool convertToTool(ToolRecord toolRecord) {
//        Tool tool = new Tool();
//        tool.setToolId(toolRecord.getToolId());
//        tool.setOwner(toolRecord.getOwner());
//        tool.setToolName(toolRecord.getToolName());
//        tool.setIsAvailable(toolRecord.getIsAvailable());
//        tool.setDescription(toolRecord.getDescription());
//        tool.setBorrower(toolRecord.getBorrower());
//        return tool;
//    }
}
