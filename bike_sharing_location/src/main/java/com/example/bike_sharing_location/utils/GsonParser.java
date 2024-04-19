package com.example.bike_sharing_location.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonParser<T> implements JsonParser<T>{
    private final Type type;
    private final Gson gson;
    public GsonParser(Type type){
        this.type = type;
        this.gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
    @Override
    public T parseJson(String json) {
        return gson.fromJson(json,type);
    }
}
