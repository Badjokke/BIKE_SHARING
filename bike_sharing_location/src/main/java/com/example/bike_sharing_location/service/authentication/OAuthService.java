package com.example.bike_sharing_location.service.authentication;

import org.springframework.http.ResponseEntity;

/**
 * interface for communication with oauth microservice
 */
public interface OAuthService {
    /**
     * Method verifies if users token is valid
     * @param token user jwt token
     * @return Response entity of 200 if token is okay, 401 otherwise
     */
    ResponseEntity<String> authenticate(String token);




}
