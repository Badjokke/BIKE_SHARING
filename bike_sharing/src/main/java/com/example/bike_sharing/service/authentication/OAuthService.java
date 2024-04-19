package com.example.bike_sharing.service.authentication;

import org.springframework.http.ResponseEntity;

public interface OAuthService {

    String generateToken(String username, String userEmail);
    ResponseEntity<String> authenticate(String token);

    void invalidateToken(String token);



}
