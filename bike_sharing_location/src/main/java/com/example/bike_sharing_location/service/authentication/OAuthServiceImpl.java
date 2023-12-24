package com.example.bike_sharing_location.service.authentication;

import com.example.bike_sharing_location.configuration.AuthServiceConfiguration;
import com.example.bike_sharing_location.http_util.HttpRequestBuilder;
import com.example.bike_sharing_location.http_util.RequestBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * implementation of auth service
 */
@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService {
    //configuration of custom auth service - url mostly - changes for local and docker run
    private final AuthServiceConfiguration authConfiguration;
    private final Logger logger = Logger.getLogger(OAuthServiceImpl.class.getName());

    public OAuthServiceImpl(AuthServiceConfiguration authConfiguration){
        this.authConfiguration = authConfiguration;
    }

    /**
     * Method validates user token
     * @param token jwt token that is sent to service for authentication (signature verification)
     * @return ResponseEntity 200 if token is valid
     *                        401 if token is invalid
     */
    public ResponseEntity<String> authenticate(String token) {
        logger.info("Authenticating token: "+token);
        RequestBuilder<String> requestBuilder = new HttpRequestBuilder(authConfiguration.getVERIFY_TOKEN_ENDPOINT());
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token);
        ResponseEntity<String> response = requestBuilder.sendPostRequest(headers,new HashMap<>());
        logger.info("Authentication response for token: "+token+" is: "+response.getStatusCode());
        return response;
    }
    //necessary for Spring boot framework - UserDetails are not used
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "",new ArrayList<>());
    }
}
