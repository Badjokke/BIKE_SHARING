package com.example.bike_sharing.authentication;

public interface OAuthService {

    String generateToken(String username, String userEmail);
    void invalidateToken(String token);

    boolean authenticate(String token);


}
