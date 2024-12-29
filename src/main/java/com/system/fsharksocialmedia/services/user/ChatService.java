package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.documents.MessageDto;
import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.reposmongo.MessageMongoReps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private MessageMongoReps messageMongoReps;

    public List<MessageDto> getMessagesBetweenUsers(String user1, String user2) {
        List<MessageModel> messages = messageMongoReps.findBySenderAndReceiverOrReceiverAndSender(user1, user2, user2, user1);

        return messages.stream()
                .filter(message -> {
                    if (message.getSender().equals(user1)) {
                        return !message.isDeletedBySender();
                    } else {
                        return !message.isDeletedByReceiver();
                    }
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteMessagesForUser(String user1, String user2, String currentUser) {
        // Lấy danh sách tin nhắn giữa hai người dùng
        List<MessageModel> messages = messageMongoReps.findBySenderAndReceiverOrReceiverAndSender(user1, user2, user2, user1);
        List<MessageModel> updatedMessages = new ArrayList<>();
        for (MessageModel message : messages) {
            if (user1.equals(currentUser) && message.getSender().equals(user1) && message.getReceiver().equals(user2)) {
                if (!message.isDeletedBySender()) {
                    message.setDeletedBySender(true);
                    System.out.println("Xóa thành công tin nhắn của sender: " + message.isDeletedBySender());
                    updatedMessages.add(message);
                }
            }
            else if (user2.equals(currentUser) && message.getSender().equals(user2) && message.getReceiver().equals(user1)) {
                if (!message.isDeletedByReceiver()) {
                    message.setDeletedByReceiver(true);
                    System.out.println("Xóa thành công tin nhắn của receiver: " + message.isDeletedByReceiver());
                    updatedMessages.add(message);
                }
            }
        }
        if (!updatedMessages.isEmpty()) {
            messageMongoReps.saveAll(updatedMessages);
        }
    }
    public MessageDto convertToDto(MessageModel messageMongo) {
        MessageDto messageDto = new MessageDto();
        messageDto.setSender(messageMongo.getSender());
        messageDto.setReceiver(messageMongo.getReceiver());
        messageDto.setContent(messageMongo.getContent());
        messageDto.setTime(messageMongo.getTime());
        return messageDto;
    }
}