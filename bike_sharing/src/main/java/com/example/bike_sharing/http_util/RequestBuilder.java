package com.example.bike_sharing.http_util;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RequestBuilder<T> {
    ResponseEntity<T> sendPostRequest(Map<String,String> headers, Map<String,Object> body);
    ResponseEntity<T> sendGetRequest(List<String> headers, Map<String,Object> body);
    ResponseEntity<T> sendPutRequest(List<String> headers, Map<String,Object> body);
    ResponseEntity<T> sendDeleteRequest(List<String> headers, Map<String,Object> body);

}
