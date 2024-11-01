package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.documents.MessageDto;
import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.services.ChatService;
import com.system.fsharksocialmedia.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserInfoService userInfoService;

    // Lấy danh sách bạn bè
    @GetMapping("list")
    public ResponseEntity<List<UserDto>> list() {
        return ResponseEntity.ok(userInfoService.getAll());
    }

    // Lấy tin nhắn giữa hai người dùng
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessagesBetweenUsers(
            @RequestParam String sender,
            @RequestParam String recipient) {
        List<MessageDto> messages = chatService.getMessagesBetweenUsers(sender, recipient);
        return ResponseEntity.ok(messages);
    }

    // Gửi tin nhắn cá nhân
    @PostMapping("/sendPrivateMessage")
    public ResponseEntity<MessageModel> sendPrivateMessage(@RequestBody MessageModel chatMessage) {
        chatService.saveMessage(chatMessage);
        System.out.println("Gửi thành công tin nhắn cá nhân: " + chatMessage);
        return ResponseEntity.ok(chatMessage);
    }

    // Gửi tin nhắn nhóm
    @PostMapping("/sendGroupMessage")
    public ResponseEntity<MessageModel> sendGroupMessage(@RequestBody MessageModel chatMessage) {
        chatService.saveMessage(chatMessage);
        System.out.println("Gửi thành công tin nhắn nhóm: " + chatMessage);
        return ResponseEntity.ok(chatMessage);
    }

    // Gửi tin nhắn qua WebSocket (cho tin nhắn cá nhân)
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageModel sendMessageWebSocket(@Payload MessageModel chatMessage) {
        chatService.saveMessage(chatMessage);
        System.out.println("Gọi sendMessage qua WebSocket với tin nhắn: " + chatMessage);
        return chatMessage;
    }

    // Gửi tin nhắn cá nhân qua WebSocket
    @MessageMapping("/chat.privateMessage")
    public void sendPrivateMessageWebSocket(@Payload MessageModel chatMessage) {
        System.out.println("Gọi privateMessage qua WebSocket với tin nhắn: " + chatMessage);
        chatService.saveMessage(chatMessage);
    }

    // Gửi tin nhắn nhóm qua WebSocket
    @MessageMapping("/chat.groupMessage")
    public void sendGroupMessageWebSocket(@Payload MessageModel chatMessage) {
        System.out.println("Gọi groupMessage qua WebSocket với tin nhắn: " + chatMessage);
        chatService.saveMessage(chatMessage);
    }
}
