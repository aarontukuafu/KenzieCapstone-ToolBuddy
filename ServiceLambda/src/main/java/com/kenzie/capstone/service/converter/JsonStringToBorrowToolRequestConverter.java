package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.kenzie.capstone.service.model.UpdateToolRequest;

public class JsonStringToBorrowToolRequestConverter {

    public UpdateToolRequest convert(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, UpdateToolRequest.class);
    }
}