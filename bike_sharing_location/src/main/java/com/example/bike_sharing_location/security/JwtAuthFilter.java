package com.example.bike_sharing_location.security;


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

public class JwtAuthFilter extends OncePerRequestFilter {

    private final OAuthService oAuthService;

    public JwtAuthFilter(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println("{\"message\" : \"Authentication failed\"}");
            return;
        }

            String token = authorizationHeader.replace("Bearer ", "");
            ResponseEntity<String> responseEntity = oAuthService.authenticate(token);
            //token is not valid
            if(responseEntity.getStatusCode().is4xxClientError() ){
                SecurityContextHolder.clearContext();
                //read response body
                String responseBody = responseEntity.getBody();
                response.setStatus(responseEntity.getStatusCode().value());
                response.getOutputStream().println(responseBody);
                return;
            }

            UserDetails userDetails = User.builder()
                    .username(responseEntity.getBody())
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
        return Arrays.stream(WhiteList.NO_AUTHORIZATION_NEEDED).anyMatch(path::startsWith);
    }


}