package com.example.bike_sharing.http_util;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RequestBuilder<T> {
    ResponseEntity<T> sendPostRequest(List<String> headers, List<Object> body);
    ResponseEntity<T> sendGetRequest(List<String> headers, List<Object> body);
    ResponseEntity<T> sendPutRequest(List<String> headers, List<Object> body);
    ResponseEntity<T> sendDeleteRequest(List<String> headers, List<Object> body);

}
