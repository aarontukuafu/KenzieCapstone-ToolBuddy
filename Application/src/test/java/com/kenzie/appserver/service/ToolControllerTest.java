package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.ToolController;
import com.kenzie.appserver.repositories.ToolRepository;
import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.service.model.Tool;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(MockitoJUnitRunner.class)
public class ToolControllerTest {

    @Mock
    private ToolService toolService;

    @InjectMocks
    private ToolController toolController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(toolController).build();
    }

    @Test
    public void getAllTools_returnsListOfToolResponses() throws Exception {
        List<Tool> tools = Arrays.asList(
                new Tool(1, "owner1", "toolName1", true, "description1", "borrower1"),
                new Tool(2, "owner2", "toolName2", true, "description2", "borrower2")
        );
        when(toolService.getAllTools()).thenReturn(tools);

        // Perform the GET request to the /tools endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/tools"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].toolId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner").value("owner1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].toolName").value("toolName1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].borrower").value("borrower1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].toolId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner").value("owner2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].toolName").value("toolName2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].borrower").value("borrower2"));
    }

}
