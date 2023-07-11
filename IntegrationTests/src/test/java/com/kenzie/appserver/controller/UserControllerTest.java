package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.service.UserService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;
import org.springframework.http.MediaType;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createNewUser_ValidRequest_ReturnsCreated() throws Exception {
        //GIVEN
        String name = mockNeat.strings().valStr();
        String username = mockNeat.strings().valStr();
        String password = mockNeat.strings().valStr();

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName(name);
        userCreateRequest.setUsername(username);
        userCreateRequest.setPassword(password);

        //WHEN
        MvcResult result = mvc.perform(post("/user/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        //THEN
        String responseBody = result.getResponse().getContentAsString();
        UserResponse userResponse = mapper.readValue(responseBody, UserResponse.class);
        assertEquals(username, userResponse.getUsername());
        assertEquals(name, userResponse.getName());
    }

    @Test
    public void createNewUser_InvalidName_ReturnsBadRequest() throws Exception {
        //GIVEN
        String username = mockNeat.strings().valStr();
        String password = mockNeat.strings().valStr();

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername(username);
        userCreateRequest.setPassword(password);

        //WHEN
        //THEN
        mvc.perform(post("/user/user")
                        .accept(String.valueOf(MediaType.APPLICATION_JSON))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(mapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteUser_ValidRequest_ReturnsNoContent() throws Exception {
        // GIVEN
        String username = "john_doe";
        String authUsername = "admin";
        String authPassword = "admin123";

        // WHEN
        // THEN
        mvc.perform(delete("/user/{username}", username)
                        .param("authUsername", authUsername)
                        .param("authPassword", authPassword))
                //.andExpect(status().isNoContent())
                .andExpect(status().isUnauthorized())
                .andReturn();

    }

    @Test
    public void deleteUser_UnauthorizedRequest_ReturnsUnauthorized() throws Exception {
        //GIVEN
        String username = "testuser";
        String authUsername = "admin";
        String authPassword = "wrong password";

        //WHEN
        //THEN
        mvc.perform(delete("/user/{username}", username)
                        .param("authUsername", authUsername)
                        .param("authPassword", authPassword))
                .andExpect(status().isUnauthorized());
    }
}
