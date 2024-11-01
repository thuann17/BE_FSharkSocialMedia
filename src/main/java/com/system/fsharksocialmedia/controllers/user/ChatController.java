//package com.system.fsharksocialmedia.controllers.user;
//
//import com.system.fsharksocialmedia.documents.MessageModel;
//import com.system.fsharksocialmedia.services.ChatService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/chat")
//public class ChatController {
//
//    @Autowired
//    private ChatService chatService;
//
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public MessageModel sendMessage(@Payload MessageModel chatMessage) {
//        chatService.saveMessage(chatMessage);
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.privateMessage")
//    public void sendPrivateMessage(@Payload MessageModel chatMessage) {
//        chatService.saveMessage(chatMessage);
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public MessageModel addUser(@Payload MessageModel chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
//
//    @GetMapping("/messages")
//    public List<MessageModel> getMessages() {
//        return chatService.getMessages();
//    }
//}