package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.ToolService;
import com.kenzie.capstone.service.converter.JsonStringToToolConverter;
import com.kenzie.capstone.service.converter.JsonStringToolRecordConverter;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.CreateToolRecordRequest;
import com.kenzie.capstone.service.model.CreateToolRequest;
import com.kenzie.capstone.service.model.ToolRecord;
import com.kenzie.capstone.service.model.ToolRecordResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GetAllTools implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        JsonStringToolRecordConverter jsonStringToolRecordConverter = new JsonStringToolRecordConverter();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        ToolService toolService = serviceComponent.provideToolLambdaService();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            //CreateToolRecordRequest createToolRecordRequest = jsonStringToolRecordConverter.convert(input.getBody());
            List<ToolRecord> toolRecordResponse = toolService.getAllTools();
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(toolRecordResponse));
        } catch (RuntimeException e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.getMessage()));
        }
    }
}
