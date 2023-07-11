//package com.kenzie.appserver.controller;
//
//import com.kenzie.appserver.controller.model.BorrowToolRequest;
//import com.kenzie.appserver.controller.model.UserCreateToolRequest;
//import com.kenzie.appserver.controller.model.UserResponse;
//import com.kenzie.appserver.service.UserService;
//import com.kenzie.capstone.service.client.LambdaServiceClient;
//import com.kenzie.capstone.service.model.Tool;
//import com.kenzie.capstone.service.model.ToolResponse;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.MediaType;
////import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureMockMvc
//public class ToolControllerIntegration {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private LambdaServiceClient toolService;
//
//    @Autowired
//    private UserService userService;
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Test
//    public void getAllTools_ReturnsNoContent() throws Exception{
//        //GIVEN
//        when(toolService.getAllTools()).thenReturn(Collections.emptyList());
//
//        //WHEN
//        mvc.perform(get("/tools"))
//                .andExpect(status().isNoContent());
//
//        //THEN
//        verify(toolService, times(1)).getAllTools();
//    }
//
//    @Test
//    public void getAllTools_ReturnsListOfTools() throws Exception{
//        //GIVEN
//        List<Tool> tools = Arrays.asList(
//                new Tool("1", "owner1", "Tool 1", true, "Description 1", null),
//                new Tool("2", "owner2", "Tool 2", true, "Description 2", null));
//
//        List<ToolResponse> toolResponses = tools.stream()
//                .map(this::createToolResponse)
//                .collect(Collectors.toList());
//
//        when(toolService.getAllTools()).thenReturn(tools);
//
//        //WHEN
//        MvcResult result = mvc.perform(get("/tools"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        //THEN
//        String responseBody = result.getResponse().getContentAsString();
//        List<ToolResponse> responses = mapper.readValue(responseBody, new TypeReference<List<ToolResponse>>(){});
//        assertEquals(toolResponses, responses);
//
//    }
//
//    @Test
//    void getAllToolsByOwnerId_ReturnsBadRequest_WhenUserNotFound() throws Exception {
//        // GIVEN
//        String ownerId = "user123";
//        when(userService.getUser(ownerId)).thenReturn(null);
//
//        // WHEN
//        mvc.perform(get("/tools/owner/{ownerId}", ownerId))
//                .andExpect(status().isBadRequest());
//
//        // THEN
//        verify(userService, times(1)).getUser(ownerId);
//        verifyNoInteractions(toolService);
//    }
//
//    @Test
//    void getAllToolsByOwnerId_ReturnsListOfTools() throws Exception {
//        // GIVEN
//        String ownerId = "user123";
//        UserResponse userRecord = new UserResponse();
//        userRecord.setUsername(ownerId);
//        when(userService.getUser(ownerId)).thenReturn(userRecord);
//
//        List<Tool> tools = Arrays.asList(
//                new Tool("1", ownerId, "Tool 1", true, "Description 1", null),
//                new Tool("2", ownerId, "Tool 2", true, "Description 2", null)
//        );
//        List<ToolResponse> toolResponses = tools.stream()
//                .map(this::createToolResponse)
//                .collect(Collectors.toList());
//        when(toolService.getAllToolsByOwnerId(ownerId)).thenReturn(tools);
//
//        // WHEN
//        MvcResult result = mvc.perform(get("/tools/owner/{ownerId}", ownerId))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // THEN
//        String responseBody = result.getResponse().getContentAsString();
//        List<ToolResponse> response = mapper.readValue(responseBody, new TypeReference<List<ToolResponse>>() {
//        });
//        assertEquals(toolResponses, response);
//    }
//
//    @Test
//    void getToolById_ReturnsNotFound_WhenToolNotFound() throws Exception {
//        // GIVEN
//        String toolId = "tool123";
//        when(toolService.getToolById(toolId)).thenReturn(null);
//
//        // WHEN
//        mvc.perform(get("/tools/tool/{toolId}", toolId))
//                .andExpect(status().isNotFound());
//
//        // THEN
//        verify(toolService, times(1)).getToolById(toolId);
//    }
//
//    @Test
//    void getToolById_ReturnsTool() throws Exception {
//        // GIVEN
//        String toolId = "tool123";
//        Tool tool = new Tool(toolId, "owner1", "Tool 1", true, "Description 1", null);
//        ToolResponse toolResponse = createToolResponse(tool);
//        when(toolService.getToolById(toolId)).thenReturn(tool);
//
//        // WHEN
//        MvcResult result = mvc.perform(get("/tools/tool/{toolId}", toolId))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // THEN
//        String responseBody = result.getResponse().getContentAsString();
//        ToolResponse response = mapper.readValue(responseBody, ToolResponse.class);
//        assertEquals(toolResponse, response);
//    }
//
//    @Test
//    void addNewTool_ReturnsCreated_WhenValidRequest() throws Exception {
//        // GIVEN
//        String username = "user123";
//        String password = "pass123";
//        String toolName = "Tool 1";
//        String description = "Description 1";
//
//        UserCreateToolRequest userCreateToolRequest = new UserCreateToolRequest();
//        userCreateToolRequest.setUsername(username);
//        userCreateToolRequest.setPassword(password);
//        userCreateToolRequest.setToolName(toolName);
//        userCreateToolRequest.setDescription(description);
//
//        when(userService.authenticator(username, password)).thenReturn(true);
//        Tool createdTool = new Tool("tool123", username, toolName, true, description, null);
//        when(toolService.addNewTool(any(Tool.class))).thenReturn(createdTool);
//
//        // WHEN
//        MvcResult result = mvc.perform(post("/tools")
//                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                        .content(mapper.writeValueAsString(userCreateToolRequest)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // THEN
//        String responseBody = result.getResponse().getContentAsString();
//        ToolResponse toolResponse = mapper.readValue(responseBody, ToolResponse.class);
//        assertEquals(createdTool.getToolId(), toolResponse.getToolId());
//        assertEquals(username, toolResponse.getOwner());
//        assertEquals(toolName, toolResponse.getToolName());
//        assertTrue(toolResponse.getIsAvailable());
//        assertEquals(description, toolResponse.getDescription());
//        assertNull(toolResponse.getBorrower());
//    }
//
//    @Test
//    void addNewTool_ReturnsBadRequest_WhenInvalidUserCredentials() throws Exception {
//        // GIVEN
//        String username = "user123";
//        String password = "pass123";
//        String toolName = "Tool 1";
//        String description = "Description 1";
//
//        UserCreateToolRequest userCreateToolRequest = new UserCreateToolRequest();
//        userCreateToolRequest.setUsername(username);
//        userCreateToolRequest.setPassword(password);
//        userCreateToolRequest.setToolName(toolName);
//        userCreateToolRequest.setDescription(description);
//
//        when(userService.authenticator(username, password)).thenReturn(false);
//
//        // WHEN
//        mvc.perform(post("/tools")
//                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                        .content(mapper.writeValueAsString(userCreateToolRequest)))
//                .andExpect(status().isBadRequest());
//
//        // THEN
//        verify(userService, times(1)).authenticator(username, password);
//        verifyNoInteractions(toolService);
//    }
//
//    @Test
//    void borrowTool_ReturnsBadRequest_WhenInvalidUserCredentials() throws Exception {
//        // GIVEN
//        String username = "user123";
//        String password = "pass123";
//        String toolId = "tool123";
//
//        BorrowToolRequest borrowToolRequest = new BorrowToolRequest();
//        borrowToolRequest.setUsername(username);
//        borrowToolRequest.setPassword(password);
//        borrowToolRequest.setToolId(toolId);
//
//        when(userService.authenticator(username, password)).thenReturn(false);
//
//        // WHEN
//        mvc.perform(put("/tools/borrowTool")
//                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                        .content(mapper.writeValueAsString(borrowToolRequest)))
//                .andExpect(status().isBadRequest());
//
//        // THEN
//        verify(userService, times(1)).authenticator(username, password);
//        verifyNoInteractions(toolService);
//    }
//
//    @Test
//    void borrowTool_ReturnsBadRequest_WhenToolNotFound() throws Exception {
//        // GIVEN
//        String username = "user123";
//        String password = "pass123";
//        String toolId = "tool123";
//
//        BorrowToolRequest borrowToolRequest = new BorrowToolRequest();
//        borrowToolRequest.setUsername(username);
//        borrowToolRequest.setPassword(password);
//        borrowToolRequest.setToolId(toolId);
//
//        when(userService.authenticator(username, password)).thenReturn(true);
//        when(toolService.getToolById(toolId)).thenReturn(null);
//
//        // WHEN
//        mvc.perform(put("/tools/borrowTool")
//                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                        .content(mapper.writeValueAsString(borrowToolRequest)))
//                .andExpect(status().isBadRequest());
//
//        // THEN
//        verify(toolService, times(1)).getToolById(toolId);
//        verifyNoMoreInteractions(toolService);
//    }
//
//    @Test
//    void borrowTool_ReturnsBadRequest_WhenToolNotAvailable() throws Exception {
//        // GIVEN
//        String username = "user123";
//        String password = "pass123";
//        String toolId = "tool123";
//        Tool tool = new Tool(toolId, "owner1", "Tool 1", false, "Description 1", null);
//
//        BorrowToolRequest borrowToolRequest = new BorrowToolRequest();
//        borrowToolRequest.setUsername(username);
//        borrowToolRequest.setPassword(password);
//        borrowToolRequest.setToolId(toolId);
//
//        when(userService.authenticator(username, password)).thenReturn(true);
//        when(toolService.getToolById(toolId)).thenReturn(tool);
//
//        // WHEN
//        mvc.perform(put("/tools/borrowTool")
//                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                        .content(mapper.writeValueAsString(borrowToolRequest)))
//                .andExpect(status().isBadRequest());
//
//        // THEN
//        verify(toolService, times(1)).getToolById(toolId);
//        verifyNoMoreInteractions(toolService);
//    }
//
//    private ToolResponse createToolResponse(Tool tool) {
//        ToolResponse toolResponse = new ToolResponse();
//        toolResponse.setToolId(tool.getToolId());
//        toolResponse.setOwner(tool.getOwner());
//        toolResponse.setToolName(tool.getToolName());
//        toolResponse.setIsAvailable(tool.getIsAvailable());
//        toolResponse.setDescription(tool.getDescription());
//        toolResponse.setBorrower(tool.getBorrower());
//        return toolResponse;
//    }
//}
