package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.model.UpdateToolRequest;

public class JsonStringToBorrowToolRequestConverter {

    public UpdateToolRequest convert(String jsonString) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, UpdateToolRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Tool could not be deserialized");
        }
    }
}