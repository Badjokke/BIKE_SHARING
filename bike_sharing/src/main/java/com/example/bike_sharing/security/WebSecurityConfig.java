package com.example.bike_sharing.security;


import com.example.bike_sharing.service.authentication.OAuth2Service;
import com.example.bike_sharing.service.authentication.OAuthService;
import com.example.bike_sharing.model.OathUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthenticationFilter;
    private final OAuth2Service oAuth2Service;
    public WebSecurityConfig(OAuthService oAuthService, OAuth2Service oAuth2Service){
        this.jwtAuthenticationFilter = new JwtAuthFilter(oAuthService);
        this.oAuth2Service = oAuth2Service;
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csfr->csfr.disable())
                .cors(cors->cors.disable())
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2Service)).successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)  {
                        try {
                            OathUser user = (OathUser) authentication.getPrincipal();
                            response.getWriter().write(user.getAccessToken());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }


}
