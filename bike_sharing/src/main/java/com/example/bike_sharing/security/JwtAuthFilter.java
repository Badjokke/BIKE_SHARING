package com.example.bike_sharing.security;

import com.example.bike_sharing.configuration.LocationServiceConfiguration;
import com.example.bike_sharing.configuration.UserServiceConfiguration;
import com.example.bike_sharing.service.authentication.OAuthService;
import com.example.bike_sharing.security.config.WhiteList;
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
import java.util.*;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final OAuthService oAuthService;
    private final LocationServiceConfiguration locationServiceConfiguration;
    public JwtAuthFilter(OAuthService oAuthService,LocationServiceConfiguration locationServiceConfiguration) {
        this.oAuthService = oAuthService;
        this.locationServiceConfiguration = locationServiceConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        String locationServiceHeader = request.getHeader("x-service");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println("{\"message\" : \"Authentication failed\"}");
            return;
        }

        String token = authorizationHeader.replace("Bearer ", "");
        String username = null;
        if(locationServiceHeader == null) {

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
            boolean isCodeOkay = locationServiceConfiguration.getLOCATION_SERVICE_CODE().equals(locationServiceHeader);
            if(isCodeOkay){
                username = "location_service";
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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return Arrays.stream(WhiteList.NO_AUTHORIZATION_NEEDED).anyMatch(path::startsWith);
    }

}