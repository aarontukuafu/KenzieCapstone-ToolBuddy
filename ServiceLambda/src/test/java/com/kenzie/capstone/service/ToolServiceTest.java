package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.ToolConverter;
import com.kenzie.capstone.service.dao.ToolDao;
import com.kenzie.capstone.service.model.CreateToolRequest;
import com.kenzie.capstone.service.model.ToolRecord;
import com.kenzie.capstone.service.model.ToolResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ToolServiceTest {
    @InjectMocks
    private ToolService toolService;
    @Mock
    private ToolDao toolDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTools_isSuccessful() {
        List<ToolRecord> toolRecords = new ArrayList<>();
        toolRecords.add(new ToolRecord(UUID.randomUUID().toString(), "Dude", "Hammer",
                true, "Smash things", "Bob"));
        toolRecords.add(new ToolRecord(UUID.randomUUID().toString(), "Bob", "Wrench",
                true, "Tighten things", "Dude"));

        when(toolDao.getAllTools()).thenReturn(toolRecords);

        List<ToolResponse> toolResponses = toolService.getAllTools();

        Assertions.assertEquals(toolRecords.size(), toolResponses.size());
        verify(toolDao, times(1)).getAllTools();
    }
    @Test
    public void getAllTools_returnsEmpty() {
        when(toolDao.getAllTools()).thenReturn(new ArrayList<>());

        List<ToolResponse> toolResponses = toolService.getAllTools();

        Assertions.assertEquals(0, toolResponses.size());

        verify(toolDao, times(1)).getAllTools();
    }
    @Test
    public void getToolById_isSuccessful() {
        String toolId = UUID.randomUUID().toString();
        ToolRecord toolRecord = new ToolRecord(toolId, "Bob", "Hammer", true,
                "smashes", "Dude");

        ToolResponse expected = new ToolResponse(toolId, "Bob", "Hammer", true,
                "smashes", "Dude");

        when(toolDao.getToolById(toolId)).thenReturn(toolRecord);

        ToolResponse actual = toolService.getToolById(toolId);

        assertEquals(expected, actual);
        verify(toolDao, times(1)).getToolById(toolId);
    }
    @Test
    public void getToolById_toolNotFound() {
        String toolId = UUID.randomUUID().toString();
        ToolRecord toolRecord = new ToolRecord();
        when(toolDao.getToolById(toolId)).thenReturn(toolRecord);

        ToolResponse response = toolService.getToolById(toolId);

        if(toolRecord == null) {
            assertNull(response);
        } else {
            assertNotNull(response);
        }
    }
    @Test
    public void addNewTool_isSuccessful() {
        CreateToolRequest toolRequest = new CreateToolRequest();
        ToolRecord toolRecord = new ToolRecord();

        Mockito.when(toolDao.addNewTool(Mockito.any(ToolRecord.class))).thenReturn(toolRecord);

        toolService = new ToolService(toolDao);

        ToolResponse toolResponse = toolService.addNewTool(toolRequest);

        Mockito.verify(toolDao, Mockito.times(1)).addNewTool(Mockito.any(ToolRecord.class));
        Assertions.assertNotNull(toolResponse);
    }
    @Test
    public void addNewTool_fails() {
        CreateToolRequest toolRequest = new CreateToolRequest();

        Mockito.doThrow(new RuntimeException("Failed to add a new tool"))
                .when(toolDao).addNewTool(Mockito.any(ToolRecord.class));
        toolService = new ToolService(toolDao);

        Assertions.assertThrows(RuntimeException.class, () -> toolService.addNewTool(toolRequest));
    }
    @Test
    public void borrowTool_isSuccessful() {
        String toolId = UUID.randomUUID().toString();
        String borrower = "Dude Man";
        ToolRecord expected = new ToolRecord(toolId, borrower);

        when(toolDao.borrowTool(toolId, borrower)).thenReturn(expected);

        toolService = new ToolService(toolDao);

        ToolRecord actual = toolService.borrowTool(toolId, borrower);

        assertEquals(expected, actual);
        verify(toolDao).borrowTool(toolId, borrower);
    }
    @Test
    public void borrowTool_fails() {
        String toolId = UUID.randomUUID().toString();
        String borrower = "Dude Man";

        when(toolDao.borrowTool(toolId, borrower)).thenReturn(null);

        toolService = new ToolService(toolDao);

        ToolRecord actual = toolService.borrowTool(toolId, borrower);

        assertNull(actual);
        verify(toolDao).borrowTool(toolId, borrower);
    }
    @Test
    public void removeTool_isSuccessful() {
        String toolId = UUID.randomUUID().toString();

        toolService.removeTool(toolId);

        verify(toolDao).removeTool(toolId);
    }
    @Test
    public void removeTool_fails() {

        String toolId = UUID.randomUUID().toString();

        RuntimeException exception = new RuntimeException("Failed to remove tool");

        doThrow(exception).when(toolDao).removeTool(toolId);
        assertThrows(RuntimeException.class, () -> {
            toolDao.removeTool(toolId);
        });
        verify(toolDao).removeTool(toolId);
    }
    @Test
    public void getAllToolsByOwnerId_isSuccessful() {
        String owner = "Dude Man";
        List<ToolRecord> expected = new ArrayList<>();
        expected.add(new ToolRecord(UUID.randomUUID().toString(), "Dude Man", "Hammer",
                true, "Smash things", "Bob"));
        expected.add(new ToolRecord(UUID.randomUUID().toString(), "Bob", "Wrench",
                true, "Tighten things", "Dude"));

        when(toolDao.getAllToolsByOwnerId(owner)).thenReturn(expected);

        List<ToolRecord> actual = toolService.getAllToolsByOwnerId(owner);

        assertEquals(expected, actual);
    }
    @Test
    public void getAllToolsByOwnerId_fails() {
        String owner = "Dude Man";
        List<ToolRecord> expected = new ArrayList<>();
        expected.add(new ToolRecord(UUID.randomUUID().toString(), "Dude Man", "Hammer",
                true, "Smash things", "Bob"));
        expected.add(new ToolRecord(UUID.randomUUID().toString(), "Bob", "Wrench",
                true, "Tighten things", "Dude"));

        when(toolDao.getAllToolsByOwnerId(owner)).thenReturn(expected);

        try {
            List<ToolRecord> actual = toolService.getAllToolsByOwnerId(owner);

            assertEquals(expected, actual);
        } catch (AssertionError e) {
            fail();
        }
    }
}
