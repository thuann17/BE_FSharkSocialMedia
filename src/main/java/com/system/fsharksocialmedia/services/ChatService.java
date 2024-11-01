package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.documents.MessageMongo;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.dtos.UserroleDto;
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

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageMongoReps messageMongoReps;
    @Autowired
    private UserroleRepository userroleRepository;

    public void saveMessage(MessageModel message) {
        try {
            MessageMongo messageMongo = new MessageMongo();
            messageMongo.setSender(message.getSender());
            messageMongo.setRecipient(message.getRecipient());
            messageMongo.setContent(message.getContent());
            messageMongo.setTimestamp(Instant.now());
            convertToModel(messageMongoReps.save(messageMongo));
            System.out.println("Gửi thành công");
            messagingTemplate.convertAndSend("/topic/public", message);
        } catch (Exception e) {
            logger.error("Error saving message: {}", e.getMessage(), e);
        }
    }

    public List<MessageModel> getMessages() {
        List<MessageMongo> messages = messageMongoReps.findAll();
        return messages.stream().map(this::convertToModel).collect(Collectors.toList());
    }

    private MessageModel convertToModel(MessageMongo messageMongo) {
        return MessageModel.builder()
                .sender(messageMongo.getSender())
                .recipient(messageMongo.getRecipient())
                .content(messageMongo.getContent())
                .time(messageMongo.getTimestamp())
                .type(MessageModel.MessageType.CHAT)
                .build();
    }

    public List<MessageMongo> getMessagesBetweenUsers(String user1, String user2) {
        return messageMongoReps.findMessagesBySenderAndRecipient(user1, user2);
    }
}
