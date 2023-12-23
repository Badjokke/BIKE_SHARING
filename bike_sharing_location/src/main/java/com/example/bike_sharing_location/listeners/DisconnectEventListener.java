package com.example.bike_sharing_location.listeners;


import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.logging.Logger;

public class DisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private static final Logger logger = Logger.getLogger(DisconnectEventListener.class.getName());

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // Your logic to handle disconnect event
        logger.info("WebSocket session disconnected: " + sessionId);
    }
}