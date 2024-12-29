package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.documents.MessageDto;
import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.reposmongo.MessageMongoReps;
import com.system.fsharksocialmedia.services.user.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageMongoReps messageMongoReps;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Lấy tin nhắn giữa hai người dùng
    @GetMapping("/api/user/chat/messages")
    public ResponseEntity<List<MessageDto>> getMessagesBetweenUsers(@RequestParam String user1, @RequestParam String user2) {
        List<MessageDto> messages = chatService.getMessagesBetweenUsers(user1, user2);
        System.out.println("Lấy tin nhắn của user1: " + user1 + ", user2: " + user2);
        return ResponseEntity.ok(messages);
    }

    // Xử lý tin nhắn đến từ client và gửi lại tin nhắn cho mọi người
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageModel sendMessage(@Payload MessageModel chatMessage) {
        try {
            chatMessage.setTime(Instant.now());
            messageMongoReps.save(chatMessage); // Lưu tin nhắn vào DB
            System.out.println("Luu tin nhan vao db");
        } catch (Exception e) {
            System.err.println("Error saving message to DB: " + e.getMessage());
            e.printStackTrace();
        }
        return chatMessage;
    }

    // check soạn tin
    @MessageMapping("/chat.typing")
    public void typing(@Payload MessageModel chatMessage) {
        messagingTemplate.convertAndSend("/topic/typing/" + chatMessage.getReceiver(), chatMessage);
    }

    // xoá tin nhắn
    @DeleteMapping("/api/deleteMessages")
    public String deleteMessages(@RequestParam String user1, @RequestParam String user2, @RequestParam String currentUser) {
        chatService.deleteMessagesForUser(user1, user2, currentUser);
        System.out.println("xoas ");
        return "Messages deleted successfully for " + currentUser;
    }
    // Xử lý khi người dùng tham gia
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public MessageModel addUser(@Payload MessageModel chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
}
