package com.example.bike_sharing_location.security;


import com.example.bike_sharing_location.service.authentication.OAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }


}
