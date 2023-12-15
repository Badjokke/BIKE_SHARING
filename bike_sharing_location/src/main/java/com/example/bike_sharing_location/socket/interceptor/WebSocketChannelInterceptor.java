package com.example.bike_sharing_location.socket.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class WebSocketChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("hello world");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
