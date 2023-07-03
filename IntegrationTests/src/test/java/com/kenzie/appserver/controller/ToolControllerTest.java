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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

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

}

