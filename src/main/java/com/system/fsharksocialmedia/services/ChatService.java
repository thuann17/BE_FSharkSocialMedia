package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.documents.MessageMongo;
import com.system.fsharksocialmedia.reposmongo.MessageMongoReps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageMongoReps messageMongoReps;

    public void saveMessage(MessageModel message) {
      try {
          MessageMongo messageMongo = new MessageMongo();
          messageMongo.setSender(message.getSender());
          messageMongo.setRecipient(message.getRecipient());
          messageMongo.setContent(message.getContent());
          messageMongo.setReciver(message.getReciver());
          messageMongo.setTimestamp(Instant.now());
          messageMongoReps.save(messageMongo);
      }catch (Exception e){
          e.printStackTrace();
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
}