package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.service.model.Tool;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.UUID.randomUUID;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolServiceTest {
    @InjectMocks
    private ToolService toolService;

    @Mock
    private ToolRepository toolRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTools_isSuccessful() {
        ToolRecord toolRecord1 = new ToolRecord(1, "owner1", "toolName1",
                true, "description1", "borrower1");
        ToolRecord toolRecord2 = new ToolRecord(2, "owner2", "toolName2",
                true, "description2", "borrower2");

        when(toolRepository.findAll()).thenReturn(Arrays.asList(toolRecord1, toolRecord2));

        List<Tool> tools = toolService.getAllTools();

        Assertions.assertEquals(2, tools.size());

        Assertions.assertEquals(1, tools.get(0).getToolId());
        Assertions.assertEquals("owner1", tools.get(0).getOwner());
        Assertions.assertEquals("toolName1", tools.get(0).getToolName());
        Assertions.assertTrue(tools.get(0).getIsAvailable());
        Assertions.assertEquals("description1", tools.get(0).getDescription());
        Assertions.assertEquals("borrower1", tools.get(0).getBorrower());

        Assertions.assertEquals(2, tools.get(1).getToolId());
        Assertions.assertEquals("owner2", tools.get(1).getOwner());
        Assertions.assertEquals("toolName2", tools.get(1).getToolName());
        Assertions.assertTrue(tools.get(1).getIsAvailable());
        Assertions.assertEquals("description2", tools.get(1).getDescription());
        Assertions.assertEquals("borrower2", tools.get(1).getBorrower());
    }

    @Test
    public void getAllTools_isNotSuccessful(){
        //GIVEN
        List<ToolRecord> emptyList = new ArrayList<>();

        //WHEN
        when(toolRepository.findAll()).thenReturn(emptyList);
        List<Tool> tools = toolService.getAllTools();

        //THEN
        Assertions.assertTrue(tools.isEmpty());
    }

//    @Test
//    public void getAllTools_handlesException(){
//        //GIVEN
//        //WHEN
//        when(toolRepository.findAll()).thenThrow(new RuntimeException("Failed to retrieve tools"));
//        List<Tool> tools = toolService.getAllTools();
//
//        //THEN
//        Assertions.assertTrue(tools.isEmpty());
//    }


    @Test
    public void viewAllTools_returnsListOfTools() {
        // GIVEN
        ToolRecord record = new ToolRecord();
        record.setToolId(12345);
        record.setOwner("owner");
        record.setToolName("toolName");
        record.setIsAvailable(true);
        record.setDescription("toolDescription");
        record.setBorrower("borrower");

        ToolRecord record2 = new ToolRecord();
        record.setToolId(54321);
        record.setOwner("owner");
        record.setToolName("toolName");
        record.setIsAvailable(false);
        record.setDescription("toolDescription");
        record.setBorrower("borrower");

        List<ToolRecord> records = new ArrayList<>();

        records.add(record);
        records.add(record2);

        when(toolRepository.findAll()).thenReturn(records);
        // WHEN

        List<Tool> tools = toolService.getAllTools();

        // THEN
        Assertions.assertNotNull(tools, "List of tools is returned");
        Assertions.assertEquals(2, tools.size(), "There are two tools in list");
    }

}
