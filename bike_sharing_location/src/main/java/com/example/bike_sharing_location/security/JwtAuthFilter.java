package com.example.bike_sharing_location.security;


import com.example.bike_sharing_location.configuration.UserServiceConfiguration;
import com.example.bike_sharing_location.security.config.WhiteList;
import com.example.bike_sharing_location.service.authentication.OAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Custom filter in security layer for validating JWT token
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final OAuthService oAuthService;
    private final UserServiceConfiguration userServiceConfiguration;

    public JwtAuthFilter(OAuthService oAuthService,UserServiceConfiguration userServiceConfiguration) {
        this.oAuthService = oAuthService;
        this.userServiceConfiguration = userServiceConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        String userServiceHeader = request.getHeader("x-service");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println("{\"message\" : \"Authentication failed\"}");
            return;
        }

            String token = authorizationHeader.replace("Bearer ", "");
            String username = null;
            if(userServiceHeader == null) {
                ResponseEntity<String> responseEntity = oAuthService.authenticate(token);
                //token is not valid
                if (responseEntity.getStatusCode().is4xxClientError()) {
                    SecurityContextHolder.clearContext();
                    //read response body
                    String responseBody = responseEntity.getBody();
                    response.setStatus(responseEntity.getStatusCode().value());
                    response.getOutputStream().println(responseBody);
                    return;
                }
                username = responseEntity.getBody();
            }
            else{
                boolean isCodeOkay = userServiceConfiguration.getUSER_SERVICE_HASH().equals(userServiceHeader);
                if(isCodeOkay){
                    username = "user_service";
                }
                else{
                    response.setStatus(401);
                    SecurityContextHolder.clearContext();
                    return;
                }
            }

            UserDetails userDetails = User.builder()
                    .username(username)
                    .password("")
                    .authorities(Collections.emptyList())
                    .build();

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);


        chain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return Arrays.asList(WhiteList.NO_AUTHORIZATION_NEEDED).contains(path);
    }


}