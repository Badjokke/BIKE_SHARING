package cz.zcu.fav.kiv.authenticator.http_util;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

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
        return this.sendRequest(headers,body,HttpMethod.POST);
    }

    @Override
    public ResponseEntity<T> sendGetRequest(Map<String,String> headers, Map<String,Object> params) {
        return this.sendRequest(headers,params,HttpMethod.GET);
    }

    @Override
    public ResponseEntity<T> sendPutRequest(Map<String,String> headers, Map<String,Object> body) {
        return this.sendRequest(headers,body,HttpMethod.PUT);
    }

    @Override
    public ResponseEntity<T> sendDeleteRequest(Map<String,String> headers, Map<String,Object> body) {
        return this.sendRequest(headers,body,HttpMethod.DELETE);
    }



    private ResponseEntity<T> sendRequest(Map<String,String> headers, Map<String,Object> body, HttpMethod method){
        //restTemplate.setErrorHandler();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        for(String key : headers.keySet()){
            httpHeaders.set(key,headers.get(key));
        }
        HttpEntity<String> requestBody = new HttpEntity<>(new Gson().toJson(body),httpHeaders);
        ResponseEntity<T> response = (ResponseEntity<T>) restTemplate.exchange(this.SERVER_URL, method,requestBody,String.class);
        return response;
    }



}
