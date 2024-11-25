package com.system.fsharksocialmedia.configs;

import com.system.fsharksocialmedia.documents.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("User disconnected: {}", username);
            // Create a 'LEAVE' message for the user that disconnected
            MessageModel chatMessage = MessageModel.builder()
//                    .type(MessageModel.MessageType.LEAVE)
                    .sender(username)
                    .content(username + " left the chat")
                    .time(Instant.now())
                    .build();
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

}
