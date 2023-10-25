package com.example.bike_sharing.http_util;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class HttpRequestBuilder<T> implements RequestBuilder<T>{
    private static final Logger logger = Logger.getLogger(HttpRequestBuilder.class.getName());

    @Override
    public ResponseEntity<T> sendPostRequest(List<String> headers, List<Object> body){
        return null;
    }

    @Override
    public ResponseEntity<T> sendGetRequest(List<String> headers, List<Object> body) {
        return null;
    }

    @Override
    public ResponseEntity<T> sendPutRequest(List<String> headers, List<Object> body) {
        return null;
    }

    @Override
    public ResponseEntity<T> sendDeleteRequest(List<String> headers, List<Object> body) {
        return null;
    }
}
