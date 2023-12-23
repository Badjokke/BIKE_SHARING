package com.example.bike_sharing_location.http_util;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Implementation of request builder for http requests
 */
public class HttpRequestBuilder implements RequestBuilder<String>{
    //endpoint to which request is going to be sent
    private final String SERVER_URL;
    //rest communication
    private final RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(HttpRequestBuilder.class.getName());
    public HttpRequestBuilder(String url){
        logger.info("Creating request builder for url: "+url);
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
        logger.info("Sending request with headers: "+headers.toString()+" with body"+body.toString()+" via method: "+method.toString());
        String url = this.SERVER_URL;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        for(String key : headers.keySet()){
            httpHeaders.set(key,headers.get(key));
        }
        httpHeaders.set("x-service","3f3b08eca62c21d76256e6e1d0b8bf99f4efbe376f64335b72f4163a8fc50dba");
        HttpEntity<String> requestBody = null;
        if(method == HttpMethod.GET){
            StringBuilder sb = new StringBuilder();
            for (String key : body.keySet()){
                sb.append(key).append("=").append(body.get(key)).append("&");
            }
            url += "?"+ sb;
        }
        requestBody = new HttpEntity<>(method!=HttpMethod.GET?new Gson().toJson(body):null,httpHeaders);
        ResponseEntity<String> response = null;
       //Object o =  restTemplate.getForEntity(url+"?userId=1",Object.class);
        try{
            logger.info("Exchanging data with url "+url);
            response = restTemplate.exchange(url, method,requestBody,String.class);
        }
        catch (HttpClientErrorException exception){
            logger.severe("Invalid request sent from server");
            response = new ResponseEntity<>(exception.getStatusCode());
        }
        catch (ResourceAccessException connectionException){
            logger.severe("Server at url: "+url+" refused to connect.");
            logger.severe(Arrays.toString(connectionException.getStackTrace()));
            throw new RuntimeException("Microservice "+this.SERVER_URL+" refused connection!");
        }
        logger.info("Request/response successful. Received: "+response);
        return response;
    }



}
