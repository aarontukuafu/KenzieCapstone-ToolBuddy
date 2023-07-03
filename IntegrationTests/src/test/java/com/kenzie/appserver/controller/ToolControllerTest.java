package com.kenzie.appserver.controller;

import com.amazonaws.services.dynamodbv2.xspec.M;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.service.ToolService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Tool;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@IntegrationTest
class ToolControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ToolService toolService;

    @Autowired
    UserService userService;

    @InjectMocks
    ToolController toolController;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(toolController).build();
    }

    @Test
    public void viewAllTools_showsList() throws Exception {
        // GIVEN

    }

    @Test
    public void getAllTools_returnsListOfToolResponses() throws Exception {
        List<Tool> tools = Arrays.asList(
                new Tool(1, "owner1", "toolName1", true, "description1", "borrower1"),
                new Tool(2, "owner2", "toolName2", true, "description2", "borrower2")
        );
        when(toolService.getAllTools()).thenReturn(tools);

        // Perform the GET request to the /tools endpoint
        mvc.perform(MockMvcRequestBuilders.get("/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].toolId").value(1))
                .andExpect(jsonPath("$[0].owner").value("owner1"))
                .andExpect(jsonPath("$[0].toolName").value("toolName1"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].description").value("description1"))
                .andExpect(jsonPath("$[0].borrower").value("borrower1"))
                .andExpect(jsonPath("$[1].toolId").value(2))
                .andExpect(jsonPath("$[1].owner").value("owner2"))
                .andExpect(jsonPath("$[1].toolName").value("toolName2"))
                .andExpect(jsonPath("$[1].available").value(true))
                .andExpect(jsonPath("$[1].description").value("description2"))
                .andExpect(jsonPath("$[1].borrower").value("borrower2"));
    }
//    @Test
//    public void getToolByOwnerId_toolDoesNotExists() throws Exception {
//        String id = UUID.randomUUID().toString();
//        mvc.perform(get("/tools/{owner}", id)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    public void removeTool_Successful() throws Exception {
//        Random random = new Random();
//        int toolId = random.nextInt();
//        String owner = mockNeat.strings().valStr();
//        String toolName = mockNeat.strings().valStr();
//        String description = mockNeat.strings().valStr();
//        String borrower = mockNeat.strings().valStr();
//
//        String user = UUID.randomUUID().toString();
//        String password = UUID.randomUUID().toString();
//
//        Tool tool = new Tool(toolId, owner, toolName, true, description, borrower);
//
//        Mockito.when(userService.authenticator(user, password)).thenReturn(true);
//        Mockito.when(toolService.findByToolName(toolName)).thenReturn(tool);
//
//        mvc.perform(delete("/toolId")
//                .param("toolId", String.valueOf(toolId))
//                .param("user", user)
//                .param("password", password))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(toolService, Mockito.times(1)).removeTool(tool,user, password);
//    }
//    @Test
//    public void removeTool_IncorrectUser() throws Exception {
//        Random random = new Random();
//        int toolId = random.nextInt();
//        String owner = mockNeat.strings().valStr();
//        String toolName = mockNeat.strings().valStr();
//        String description = mockNeat.strings().valStr();
//        String borrower = mockNeat.strings().valStr();
//
//        String user = UUID.randomUUID().toString();
//        String password = UUID.randomUUID().toString();
//
//        Tool tool = new Tool(toolId, owner, toolName, true, description, borrower);
//
//        Mockito.when(userService.authenticator(user, password)).thenReturn(true);
//
//        mvc.perform(delete("/toolId")
//                        .param("toolId", String.valueOf(toolId))
//                        .param("user", user)
//                        .param("password", password))
//                .andExpect(status().isBadRequest());
//
//        Mockito.verify(toolService, Mockito.never()).removeTool(Mockito.any(Tool.class),
//                Mockito.anyString(), Mockito.anyString());
//    }
//    @Test
//    public void addNewTool_Successful() throws Exception {
//        Mockito.when(userService.authenticator(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
//
//        String request = "{\"name\" : \"Tool Name\"}";
//
//        mvc.perform(MockMvcRequestBuilders.post("/tools")
//                .param("username", "testUser")
//                .param("password", "testPassword")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(request))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
}