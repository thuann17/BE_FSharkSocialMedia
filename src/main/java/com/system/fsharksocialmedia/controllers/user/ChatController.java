package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.documents.MessageDto;
import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.services.ChatService;
import com.system.fsharksocialmedia.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Lấy danh sách bạn bè

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working");
    }

    // Lấy tin nhắn giữa hai người dùng
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessagesBetweenUsers(@RequestParam String user1, @RequestParam String user2) {
        List<MessageDto> messages = chatService.getMessagesBetweenUsers(user1, user2);
        System.out.println("Lấy tin nhắn của user1: " + user1 + ", user2: " + user2);
        return ResponseEntity.ok(messages);
    }

    // Gửi tin nhắn cá nhân qua WebSocket
    @MessageMapping("/chat/{receiver}")
    public void sendMessage(@Payload MessageModel chatMessage, @DestinationVariable String receiver) {
        try {
            chatService.saveMessage(chatMessage);
            String destination = "/user/" + chatMessage.getReciver() + "/queue/messages";
            messagingTemplate.convertAndSend(destination, chatMessage);

            System.out.println("gửi thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    // Gửi tin nhắn nhóm qua WebSocket
    @MessageMapping("/chat.groupMessage")
    public void sendGroupMessageWebSocket(@Payload MessageModel chatMessage) {
        System.out.println("Gọi groupMessage qua WebSocket với tin nhắn: " + chatMessage);
        chatService.saveMessage(chatMessage);
    }
}
