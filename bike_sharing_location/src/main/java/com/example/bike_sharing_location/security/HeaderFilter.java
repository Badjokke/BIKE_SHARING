package com.example.bike_sharing_location.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * NOT USED
 * should strip all custom headers from http request
 */
public class HeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        stripHeaders(request);
        filterChain.doFilter(request, response);
    }
    private void stripHeaders(HttpServletRequest request) {
        Set<String> headersToKeep = new HashSet<>(Arrays.asList(
                "Content-Type",
                "Authorization",
                "Accept",
                "Accept-Encoding",
                "Accept-Language",
                "Cache-Control",
                "Connection",
                "Host",
                "Origin",
                "Referer",
                "User-Agent")
        );
        Iterator<String> headerIterator = request.getHeaderNames().asIterator();
        while(headerIterator.hasNext()){
            String header = headerIterator.next();
            if (!headersToKeep.contains(header))
                request.removeAttribute(header);
        }


    }

}
