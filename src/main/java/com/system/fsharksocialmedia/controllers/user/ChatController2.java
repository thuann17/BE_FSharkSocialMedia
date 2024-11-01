package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.services.ChatService;
import com.system.fsharksocialmedia.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/chat")
public class ChatController2 {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserInfoService userInfoService;

    //    Lay danh sach ban be
        @GetMapping("/list")
        public ResponseEntity<List<UserDto>> getListFriend() { // sử lí tạm
            return ResponseEntity.ok(userInfoService.getAll());
        }

    // Xử lý gửi tin nhắn qua yêu cầu POST
    @PostMapping("/sendMessage")
    public ResponseEntity<MessageModel> sendMessage(@RequestBody MessageModel chatMessage) {
        chatService.saveMessage(chatMessage);
        System.out.println("Gửi thành công tin nhắn: " + chatMessage);
        return ResponseEntity.ok(chatMessage);
    }

    // Phương thức này sẽ vẫn dùng cho WebSocket
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageModel sendMessageWebSocket(@Payload MessageModel chatMessage) {
        chatService.saveMessage(chatMessage);
        System.out.println("Gọi sendMessage qua WebSocket với tin nhắn: " + chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.privateMessage")
    public void sendPrivateMessage(@Payload MessageModel chatMessage) {
        System.out.println("Gọi privateMessage với tin nhắn: " + chatMessage);
        chatService.saveMessage(chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public MessageModel addUser(@Payload MessageModel chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
