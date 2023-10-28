package com.example.bike_sharing.authentication;

import com.example.bike_sharing.configuration.AuthConfiguration;
import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.http_util.HttpRequestBuilder;
import com.example.bike_sharing.http_util.RequestBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService {
    private final AuthConfiguration authConfiguration;

    public OAuthServiceImpl(AuthConfiguration authConfiguration){
        this.authConfiguration = authConfiguration;
    }

    @Override
    public String generateToken(String username, String userEmail) {
        RequestBuilder<String> requestBuilder = new HttpRequestBuilder<>(authConfiguration.getGENERATE_TOKEN_ENDPOINT());
        Map<String,Object> body = new HashMap<>();
        body.put("name",userEmail);
        Map<String,String> headers = new HashMap<>();
        ResponseEntity<String> response = requestBuilder.sendPostRequest(headers,body);
        String tmpBody = response.getBody();
        return null;
    }

    @Override
    public void invalidateToken(String token) {

    }

    @Override
    public ResponseEntity<String> authenticate(String token) {
        return ResponseEntity.status(200).body("{\"token\":\"ahoj_svete\"}");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "",new ArrayList<>());
    }
}
