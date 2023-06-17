package com.kenzie.appserver.controller;



import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.service.ToolService;
import com.kenzie.appserver.service.model.Tool;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;
import java.util.UUID;

public class ToolContollerTest {
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


    }
}
