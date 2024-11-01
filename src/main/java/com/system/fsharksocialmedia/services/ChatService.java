package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.documents.MessageDto;
import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.documents.MessageMongo;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.dtos.UserroleDto;
import com.system.fsharksocialmedia.entities.Message;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.entities.Userrole;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.repositories.UserroleRepository;
import com.system.fsharksocialmedia.reposmongo.MessageMongoReps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageMongoReps messageMongoReps;

    //Lấy đoạn chat 2 user
    public List<MessageDto> getMessagesBetweenUsers(String user1, String user2) {
        List<MessageMongo> messages = messageMongoReps.findBySenderAndReciver(user1, user2); // Adjust this method to your repository
        return messages.stream()
                .map(this::converToDto)
                .collect(Collectors.toList());
    }

    public MessageDto saveMessage(MessageModel message) {
        try {
            MessageMongo messageMongo = new MessageMongo();
            messageMongo.setSender(message.getSender());
            messageMongo.setReciver(message.getRecipient());
            messageMongo.setContent(message.getContent());
            messageMongo.setTimestamp(Instant.now());
            MessageMongo saved = messageMongoReps.save(messageMongo);
            System.out.println("Gửi thành công");
            messagingTemplate.convertAndSend("/topic/public", message);
            return converToDto(saved);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MessageDto converToDto(MessageMongo messageMongo) {
        MessageDto messageDto = new MessageDto();
        messageDto.setSender(messageMongo.getSender());
        messageDto.setReciver(messageMongo.getReciver());
        messageDto.setContent(messageMongo.getContent());
        messageDto.setTimestamp(messageMongo.getTimestamp());
        return messageDto;
    }

}
