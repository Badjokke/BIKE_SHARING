package com.example.bike_sharing_location.http_util;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.logging.Logger;

public class HttpRequestBuilder implements RequestBuilder<String>{
    private final String SERVER_URL;
    private final RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(HttpRequestBuilder.class.getName());
    public HttpRequestBuilder(String url){
        this.SERVER_URL = url;
        this.restTemplate = new RestTemplate();
    }
    @Override
    public ResponseEntity<String> sendPostRequest(Map<String,String> headers, Map<String,Object> body){
        //restTemplate.setErrorHandler();
        return this.sendRequest(headers,body,HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> sendGetRequest(Map<String,String> headers, Map<String,Object> params) {

        return this.sendRequest(headers,params,HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> sendPutRequest(Map<String,String> headers, Map<String,Object> body) {
        return this.sendRequest(headers,body,HttpMethod.PUT);
    }

    @Override
    public ResponseEntity<String> sendDeleteRequest(Map<String,String> headers, Map<String,Object> body) {
        return this.sendRequest(headers,body,HttpMethod.DELETE);
    }



    private ResponseEntity<String> sendRequest(Map<String,String> headers, Map<String,Object> body, HttpMethod method){
        //restTemplate.setErrorHandler();
        String url = this.SERVER_URL;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        for(String key : headers.keySet()){
            httpHeaders.set(key,headers.get(key));
        }
        HttpEntity<String> requestBody = null;
        if(method == HttpMethod.GET){
            StringBuilder sb = new StringBuilder();
            for (String key : body.keySet()){
                sb.append(key).append("=").append(body.get(key)).append("&");
            }
            url += "?"+ sb;
        }
        else{
            requestBody = new HttpEntity<>(new Gson().toJson(body),httpHeaders);
        }
        ResponseEntity<String> response = null;
       //Object o =  restTemplate.getForEntity(url+"?userId=1",Object.class);
        try{
            response = restTemplate.exchange(url, method,requestBody,String.class);
        }
        catch (HttpClientErrorException exception){
            response = new ResponseEntity<>(exception.getStatusCode());
        }
        catch (ResourceAccessException connectionException){
            //throw new RuntimeException("Microservice "+this.SERVER_URL+" refused connection!");
            return null;
        }
        return response;
    }



}
