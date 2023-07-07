package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.model.CreateToolRequest;

public class JsonStringToToolConverter {

    public CreateToolRequest convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(body, CreateToolRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Tool could not be deserialized");
        }
    }
}
