package com.example.bike_sharing.http_util;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HttpRequestBuilder<T> implements RequestBuilder<T>{
    private final String SERVER_URL;
    private final RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(HttpRequestBuilder.class.getName());
    public HttpRequestBuilder(String url){
        this.SERVER_URL = url;
        this.restTemplate = new RestTemplate();
    }
    @Override
    public ResponseEntity<T> sendPostRequest(Map<String,String> headers, Map<String,Object> body){
        //restTemplate.setErrorHandler();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        for(String key : headers.keySet()){
            httpHeaders.set(key,headers.get(key));
        }
        HttpEntity<String> requestBody = new HttpEntity<>(new Gson().toJson(body),httpHeaders);
        ResponseEntity<T> response = (ResponseEntity<T>) restTemplate.exchange(this.SERVER_URL, HttpMethod.POST,requestBody,String.class);
        return response;
    }

    @Override
    public ResponseEntity<T> sendGetRequest(List<String> headers, Map<String,Object> body) {
        return null;
    }

    @Override
    public ResponseEntity<T> sendPutRequest(List<String> headers, Map<String,Object> body) {
        return null;
    }

    @Override
    public ResponseEntity<T> sendDeleteRequest(List<String> headers, Map<String,Object> body) {
        return null;
    }
}
