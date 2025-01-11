package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.documents.MessageDto;
import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.reposmongo.MessageMongoReps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private MessageMongoReps messageMongoReps;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Retrieve messages between two users
    public List<MessageDto> getMessagesBetweenUsers(String user1, String user2) {
        List<MessageModel> messages = messageMongoReps.findMessagesForUsers(user1, user2);
        System.out.println(messages);
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MessageModel saveMessage(MessageModel chatMessage) {
        try {
            if (chatMessage.getUrlImage() == null) {
                chatMessage.setUrlImage(null);
            } else {
                chatMessage.setUrlImage(chatMessage.getUrlImage());
            }
            chatMessage.setTime(Instant.now());
            chatMessage.setDeletedBySender(false);
            chatMessage.setDeletedByReceiver(false);
            chatMessage.setReal(false);
            messagingTemplate.convertAndSend("/topic/public/" + chatMessage.getSender(), chatMessage);
            messageMongoReps.save(chatMessage);
        } catch (Exception e) {
            System.err.println("Error saving message to DB: " + e.getMessage());
            e.printStackTrace();
        }
        return chatMessage;
    }

    public void sendReply(MessageModel model, String parentMessageId) {
        MessageModel chatMessage = messageMongoReps.findById(model.getParentMessageId()).orElse(null);
        chatMessage.setParentMessageId(parentMessageId);
        try {
            if (chatMessage.getUrlImage() == null) {
                chatMessage.setUrlImage("");
            } else {
                chatMessage.setUrlImage(chatMessage.getUrlImage());
            }
            chatMessage.setTime(Instant.now());
            chatMessage.setDeletedBySender(false);
            chatMessage.setDeletedByReceiver(false);
            chatMessage.setReal(false);

            messagingTemplate.convertAndSend("/topic/public/" + chatMessage.getSender(), chatMessage);
            messageMongoReps.save(chatMessage);
        } catch (Exception e) {
            System.err.println("Error saving message to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<MessageDto> getUnreadMessages(String receiver) {
        List<MessageModel> unreadMessages = messageMongoReps.findByReceiverAndReal(receiver, false);
        return unreadMessages.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    // Convert MessageModel to MessageDto
    public MessageDto convertToDto(MessageModel messageMongo) {
        MessageDto messageDto = new MessageDto();
        messageDto.setSender(messageMongo.getSender());
        messageDto.setReceiver(messageMongo.getReceiver());
        messageDto.setContent(messageMongo.getContent());
        messageDto.setTime(messageMongo.getTime());
        messageDto.setUrlImage(messageMongo.getUrlImage());
        messageDto.setReal(messageMongo.isReal());
        return messageDto;
    }
}
