package com.example.bike_sharing.security;


import com.example.bike_sharing.authentication.OAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthenticationFilter;
    public WebSecurityConfig(OAuthService oAuthService){
        this.jwtAuthenticationFilter = new JwtAuthFilter(oAuthService);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(
                jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).csrf(csfr->csfr.disable()).cors(cors->cors.disable());
        return http.build();
    }


}
