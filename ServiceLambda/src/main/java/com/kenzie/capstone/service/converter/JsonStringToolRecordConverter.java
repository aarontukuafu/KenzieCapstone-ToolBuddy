package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.model.CreateToolRecordRequest;
import com.kenzie.capstone.service.model.CreateToolRequest;
import com.kenzie.capstone.service.model.ToolRecord;

public class JsonStringToolRecordConverter {
    public CreateToolRecordRequest convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(body, CreateToolRecordRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("ToolRecord could not be deserialized");
        }
    }
}
