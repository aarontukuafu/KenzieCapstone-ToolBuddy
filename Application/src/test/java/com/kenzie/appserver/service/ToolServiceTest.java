package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.UserRecordRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Tool;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.util.*;

import static java.util.UUID.randomUUID;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.times;

public class ToolServiceTest {
    @InjectMocks
    private ToolService toolService;
    @Mock
    private UserService userService;

    @Mock
    private ToolRepository toolRepository;
    @Mock
    private UserRecordRepository userRecordRepository;
    @Mock
    private CacheStore cache;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        toolService = new ToolService(toolRepository, userService, cache);
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

    @Test
    public void getAllToolsForUserById_isSuccessful() {
        ToolRecord testToolRecord = new ToolRecord();
        testToolRecord.setOwner("owner1");
        testToolRecord.setToolId(1);
        ToolRecord testToolRecord2 = new ToolRecord();
        testToolRecord2.setOwner("owner1");
        testToolRecord2.setToolId(2);


        when(toolRepository.findByOwner("owner1")).thenReturn(Arrays.asList(testToolRecord, testToolRecord2));

        List<Tool> testList = toolService.getAllToolsByOwnerId("owner1");

        assertEquals(2, testList.size());
        assertEquals("owner1", testList.get(0).getOwner());
        assertEquals("owner1", testList.get(1).getOwner());
    }
    @Test
    public void getAllToolsByOwnerId_isSuccessful() {
        String owner = randomUUID().toString();
        List<ToolRecord> toolRecords = new ArrayList<>();
        toolRecords.add(new ToolRecord());

        Mockito.when(toolRepository.findByOwner(owner)).thenReturn(toolRecords);

        List<Tool> tools = toolService.getAllToolsByOwnerId(owner);

        Assert.assertNotNull(tools);

        Assert.assertEquals(toolRecords.size(), tools.size());
    }
    @Test
    public void removeTool_isSuccessful() {
        Tool tool = new Tool();
        String username = randomUUID().toString();
        String password = randomUUID().toString();

        when(userService.authenticator(username,password)).thenReturn(true);
        toolService.removeTool(tool,username,password);

        verify(userService).authenticator(username,password);
        verify(toolRepository).delete(any(ToolRecord.class));
    }
    @Test
    public void removeTool_Fails() {
        Tool tool = new Tool();
        String username = randomUUID().toString();
        String password = randomUUID().toString();

        when(userService.authenticator(username,password)).thenReturn(false);
        toolService.removeTool(tool,username,password);

        verify(userService).authenticator(username,password);
        verify(toolRepository,never()).delete(any(ToolRecord.class));
    }
    @Test
    public void addNewTool_isSuccessful() {
        String username = randomUUID().toString();
        String password = randomUUID().toString();
        Tool tool = new Tool();

        when(userService.authenticator(username,password)).thenReturn(true);
        toolService.addNewTool(tool,username, password);

       verify(userService).authenticator(username,password);
    }
    @Test
    public void addNewTool_fails() {
        String username = randomUUID().toString();
        String password = randomUUID().toString();
        Tool tool = new Tool();

        when(userService.authenticator(username,password)).thenReturn(false);
        toolService.addNewTool(tool,username, password);

        verify(userService).authenticator(username,password);
    }
    @Test
    public void findByToolName_cacheExists() {
        String toolName = "Mjolnir";
        Tool cachedTool = new Tool(666, "Dude", toolName, true, "Cant lift", "Steve");
        when(cache.get(toolName)).thenReturn(cachedTool);

        Tool result = toolService.findByToolName(toolName);

        Assertions.assertEquals(cachedTool, result);
        verify(cache, never()).add(anyString(), any(Tool.class));
        verify(toolRepository, never()).findById(anyString());
    }
//    @Test
//    public void findToolName_backendExists() {
//        String toolName = "Wrench";
//        Tool backendTool = new Tool(111, "ManDude", toolName, true, "tool", "Bob");
//        when(cache.get(toolName)).thenReturn(null);
//        when(toolRepository.findById(toolName)).thenReturn(Optional.of(backendTool));
//
//        Tool result = toolService.findByToolName(toolName);
//
//        Assertions.assertEquals(backendTool, result);
//        verify(cache).add(toolName, backendTool);
//        verify(toolRepository).findById(toolName);
//    }
    @Test
    public void findToolName_backendDoesNotExist() {
        String toolName = "Disintegrator Ray";
        when(cache.get(toolName)).thenReturn(null);
        when(toolRepository.findById(toolName)).thenReturn(Optional.empty());

        Tool result = toolService.findByToolName(toolName);

        Assertions.assertNull(result);
        verify(cache, never()).add(anyString(), any(Tool.class));
    }
}
