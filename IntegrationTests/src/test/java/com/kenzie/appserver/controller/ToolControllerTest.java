package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.service.ToolService;
import com.kenzie.appserver.service.model.Tool;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

public class ToolControllerTest {
    @Autowired
    ToolService toolService;
    @Autowired
    private MockMvc mvc;
    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void viewAllTools_showsList() throws Exception {
        // GIVEN
    }
}
