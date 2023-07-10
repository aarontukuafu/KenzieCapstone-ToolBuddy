package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.ToolService;
import com.kenzie.capstone.service.converter.JsonStringRemoveToolConverter;
import com.kenzie.capstone.service.converter.JsonStringToolRecordConverter;
import com.kenzie.capstone.service.converter.ToolConverter;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.CreateToolRecordRequest;
import com.kenzie.capstone.service.model.ToolRecord;
import com.kenzie.capstone.service.model.ToolResponse;
import com.kenzie.capstone.service.model.UpdateToolRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RemoveTool implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        JsonStringRemoveToolConverter jsonStringRemoveToolConverter = new JsonStringRemoveToolConverter();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        ToolService toolService = serviceComponent.provideToolLambdaService();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            UpdateToolRequest removeToolRequest = jsonStringRemoveToolConverter.convert(input.getBody());
            ToolRecord toolRecord = toolService.removeTool(removeToolRequest.getToolId());
            ToolResponse toolResponse = ToolConverter.fromRecordToResponse(toolRecord);
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(toolResponse));
        } catch (RuntimeException e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.getMessage()));
        }
    }
}

