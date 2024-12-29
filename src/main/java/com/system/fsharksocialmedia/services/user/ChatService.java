package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.documents.MessageDto;
import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.reposmongo.MessageMongoReps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private MessageMongoReps messageMongoReps;

    //Lấy đoạn chat 2 user
    public List<MessageDto> getMessagesBetweenUsers(String user1, String user2) {
        List<MessageModel> messages = messageMongoReps.findMessagesBetweenUsers(user1, user2);
        return messages.stream()
                .map(this::converToDto)
                .collect(Collectors.toList());
    }


    public void deleteMessagesForUser(String user1, String user2, String currentUser) {
        if (user1.equals(currentUser)) {
            // Xóa tin nhắn của user1
            messageMongoReps.deleteBySenderAndReceiver(user1, user2);
        } else if (user2.equals(currentUser)) {
            // Xóa tin nhắn của user2
            messageMongoReps.deleteBySenderAndReceiver(user2, user1);
        }
    }

    public MessageDto converToDto(MessageModel messageMongo) {
        MessageDto messageDto = new MessageDto();
        messageDto.setSender(messageMongo.getSender());
        messageDto.setReceiver(messageMongo.getReceiver());
        messageDto.setContent(messageMongo.getContent());
        messageDto.setTimestamp(messageMongo.getTime());
        return messageDto;
    }
}
