package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.service.ToolService;
import com.kenzie.appserver.service.model.Tool;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.module.SimpleModule;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
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
}