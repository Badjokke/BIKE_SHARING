package com.example.bike_sharing.security;
import com.example.bike_sharing.configuration.ClientAppConfiguration;
import com.example.bike_sharing.configuration.LocationServiceConfiguration;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthenticationFilter;
    private final HeaderFilter headerFilter;
    private final ClientAppConfiguration clientAppConfiguration;
    private final OAuth2Service oAuth2Service;
    public WebSecurityConfig(OAuthService oAuthService, OAuth2Service oAuth2Service, ClientAppConfiguration clientAppConfiguration, LocationServiceConfiguration locationServiceConfiguration){
        this.jwtAuthenticationFilter = new JwtAuthFilter(oAuthService,locationServiceConfiguration);
        this.headerFilter = new HeaderFilter();
        this.oAuth2Service = oAuth2Service;
        this.clientAppConfiguration = clientAppConfiguration;
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csfr->csfr.disable())
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2Service)).successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)  {
                        try {
                            OathUser user = (OathUser) authentication.getPrincipal();
                            response.sendRedirect(clientAppConfiguration.getRedirectUrl()+"?token="+user.getAccessToken()+"&email="+user.getEmail()+"&role=REGULAR");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                //.addFilterAfter(headerFilter, JwtAuthFilter.class);



        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(clientAppConfiguration.getClientUrl()));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
