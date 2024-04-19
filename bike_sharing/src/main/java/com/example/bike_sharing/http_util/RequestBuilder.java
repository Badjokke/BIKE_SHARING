package com.example.bike_sharing.http_util;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RequestBuilder<T> {
    ResponseEntity<T> sendPostRequest(Map<String,String> headers, Map<String,Object> body);
    ResponseEntity<T> sendGetRequest(Map<String,String> headers, Map<String,Object> params);
    ResponseEntity<T> sendPutRequest(Map<String,String> headers, Map<String,Object> body);
    ResponseEntity<T> sendDeleteRequest(Map<String,String> headers, Map<String,Object> body);

}
