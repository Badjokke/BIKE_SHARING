package com.example.bike_sharing_location.socket.interceptor;

import com.example.bike_sharing_location.service.ride.RideService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final RideService rideService;
    public WebSocketHandshakeInterceptor(RideService service){
        this.rideService = service;


    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        //token is not provided in query
        if(query == null || query.length() == 0){
            return false;
        }
        String[] queryParams = query.split("=");
        //invalid query params - should be rideToken=tokenValue
        if(queryParams.length != 2){
            return false;
        }
        String token = queryParams[1];
        return this.rideService.rideExists(token);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        //dont really care
    }


}
