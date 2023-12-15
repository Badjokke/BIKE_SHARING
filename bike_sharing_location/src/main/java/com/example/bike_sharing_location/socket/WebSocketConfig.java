package com.example.bike_sharing_location.socket;


import com.example.bike_sharing_location.listeners.DisconnectEventListener;
import com.example.bike_sharing_location.service.ride.RideService;
import com.example.bike_sharing_location.socket.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final RideService service;
    public WebSocketConfig(RideService service){
        this.service =service;
    }
    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new WebSocketHandshakeInterceptor(service);
    }
    /**@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketChannelInterceptor());
    }*/
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/bike_location","/bike_ride");
        config.setApplicationDestinationPrefixes("/bike_sharing");
    }
    @Bean
    public DisconnectEventListener disconnectEventListener() {
        return new DisconnectEventListener();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/bike").setAllowedOrigins("*");
        registry.addEndpoint("/ride/**")
                .addInterceptors(handshakeInterceptor())
                .setAllowedOrigins("*");
    }


}
