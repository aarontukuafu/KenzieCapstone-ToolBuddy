package com.kenzie.appserver.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.service.ToolService;
import com.kenzie.appserver.service.model.Tool;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.module.SimpleModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Random;

@IntegrationTest
class ToolControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ToolService toolService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getTool_ToolExists() throws Exception {
        Random random = new Random();

        int id = random.nextInt();
        String owner = mockNeat.strings().valStr();
        String toolName = mockNeat.strings().valStr();
        boolean isAvailable = true;
        String description = mockNeat.strings().valStr();
        String borrower = mockNeat.strings().valStr();

        Tool tool = new Tool(id, owner, toolName, isAvailable, description, borrower);
        Tool existingTool = toolService.addNewTool(tool);

        mapper.registerModule(new SimpleModule());
        //WHEN
        mvc.perform(get("/tools/{toolId}", existingTool.getToolId())
                        .accept(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(jsonPath("id")
                        .value(is(id)))
                .andExpect(jsonPath("owner")
                        .value(is(owner)))
                .andExpect(jsonPath("toolName")
                        .value(is(toolName)))
                .andExpect(jsonPath("isAvailable")
                        .value(is(false)))
                .andExpect(jsonPath("description")
                        .value(is(description)))
                .andExpect(jsonPath("borrower")
                        .value(is(borrower)))
                .andExpect(status().isOk());
    }
}