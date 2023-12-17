package com.example.bike_sharing.service.authentication;

import com.example.bike_sharing.configuration.AuthServiceConfiguration;
import com.example.bike_sharing.http_util.HttpRequestBuilder;
import com.example.bike_sharing.http_util.RequestBuilder;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService {
    private final AuthServiceConfiguration authConfiguration;

    public OAuthServiceImpl(AuthServiceConfiguration authConfiguration){
        this.authConfiguration = authConfiguration;
    }

    @Override
    public String generateToken(String username, String userEmail) {
        RequestBuilder<String> requestBuilder = new HttpRequestBuilder(authConfiguration.getGENERATE_TOKEN_ENDPOINT());
        Map<String,Object> body = new HashMap<>();
        body.put("name",userEmail);
        Map<String,String> headers = new HashMap<>(); 
        ResponseEntity<String> response = requestBuilder.sendPostRequest(headers,body);
        if(response == null){
            throw new RuntimeException("Auth service down.");
        }
        String tmpBody = response.getBody();
        Map<String,String> responseBody = new Gson().fromJson(tmpBody,Map.class);

        return responseBody.get("token");
    }

    @Override
    public void invalidateToken(String token) {

    }

    public ResponseEntity<String> authenticate(String token) {
        RequestBuilder<String> requestBuilder = new HttpRequestBuilder(authConfiguration.getVERIFY_TOKEN_ENDPOINT());
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token);
        ResponseEntity<String> response = requestBuilder.sendPostRequest(headers,new HashMap<>());
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "",new ArrayList<>());
    }
}
