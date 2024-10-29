package com.system.fsharksocialmedia.configs;

import com.system.fsharksocialmedia.documents.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;
    private static final String PUBLIC_TOPIC = "/topic/public";

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getSessionAttributes() != null) {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            if (username != null) {
                log.info("User disconnected: {}", username);
                var chatMessage = MessageModel.builder()
                        .type(MessageModel.MessageType.LEAVE)
                        .sender(username)
                        .build();
                messagingTemplate.convertAndSend(PUBLIC_TOPIC, chatMessage);
            }
        }
    }
}