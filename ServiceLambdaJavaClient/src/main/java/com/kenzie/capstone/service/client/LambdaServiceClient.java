package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.converter.ToolConverter;
import com.kenzie.capstone.service.model.CreateToolRequest;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.Tool;
import com.kenzie.capstone.service.model.ToolResponse;

import java.lang.reflect.Type;
import java.util.List;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
    private static final String SET_EXAMPLE_ENDPOINT = "example";
    private static final String GET_ALL_TOOLS_ENDPOINT = "tools/getall";
    private static final String GET_ALL_TOOLS_BY_OWNER_ENDPOINT = "/owner/{ownerid}";
    private static final String GET_TOOL_BY_ID_ENDPOINT = "/tool/{toolid}";
    private static final String POST_NEW_TOOL_ENDPOINT = "tools"; //Can also be post
    private static final String PUT_BORROW_TOOL_ENDPOINT = "borrowtool";
    private static final String DELETE_TOOL_ENDPOINT = "tools/delete"; //uses post NOT delete
    // private static final String  = "";
    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public ExampleData getExampleData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_EXAMPLE_ENDPOINT.replace("{id}", id));
        ExampleData exampleData;
        try {
            exampleData = mapper.readValue(response, ExampleData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return exampleData;
    }

    public ExampleData setExampleData(String data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(SET_EXAMPLE_ENDPOINT, data);
        ExampleData exampleData;
        try {
            exampleData = mapper.readValue(response, ExampleData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return exampleData;
    }

    public List<Tool> getAllTools() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALL_TOOLS_ENDPOINT);
        List<Tool> tools;
        try {
            tools = mapper.readValue(response, new TypeReference<List<Tool>>() {
            });
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return tools;
    }

    public List<Tool> getAllToolsByOwnerId(String ownerId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALL_TOOLS_BY_OWNER_ENDPOINT.replace("{ownerid}", ownerId));
        List<Tool> tools;
        try {
            tools = mapper.readValue(response, new TypeReference<List<Tool>>() {
            });
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return tools;
    }

    public Tool getToolById(String toolId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_TOOL_BY_ID_ENDPOINT.replace("{toolid}", toolId));
        Tool tool;
        try {
            tool = mapper.readValue(response, Tool.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return tool;
    }

    public Tool addNewTool(Tool tool) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = null;
        CreateToolRequest createToolRequest = ToolConverter.fromToolToRequest(tool);

        try {
            response = endpointUtility.postEndpoint(POST_NEW_TOOL_ENDPOINT, mapper.writeValueAsString(createToolRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Tool createdTool;
        try {
            createdTool = mapper.readValue(response, Tool.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return createdTool;
    }

    public Tool borrowTool(String toolId, String borrower) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(PUT_BORROW_TOOL_ENDPOINT.replace("{toolid}", toolId), borrower);
        Tool borrowedTool;
        try {
            borrowedTool = mapper.readValue(response, Tool.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return borrowedTool;
    }

    public void deleteTool(String toolId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        try {
            String response = endpointUtility.postEndpoint(DELETE_TOOL_ENDPOINT.replace("{id}", toolId), null);
            if (!response.contains("success")) {
                throw new ApiGatewayException("Failed to delete the tool with id: " + toolId);
            }
        } catch (Exception e) {
            throw new ApiGatewayException("Failed to delete the tool: " + e.getMessage());
        }
    }
}
