package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.ToolService;
import com.kenzie.capstone.service.converter.JsonStringToBorrowToolRequestConverter;
import com.kenzie.capstone.service.converter.ToolConverter;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.ToolRecord;
import com.kenzie.capstone.service.model.UpdateToolRequest;
import com.kenzie.capstone.service.model.ToolResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BorrowTool implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        JsonStringToBorrowToolRequestConverter jsonStringToBorrowToolRequestConverter = new JsonStringToBorrowToolRequestConverter();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        ToolService toolService = serviceComponent.provideToolLambdaService();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            UpdateToolRequest borrowToolRequest = jsonStringToBorrowToolRequestConverter.convert(input.getBody());
            ToolRecord toolRecord = toolService.borrowTool(borrowToolRequest.getToolId(), borrowToolRequest.getUsername());
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