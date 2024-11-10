package com.system.fsharksocialmedia.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
//@Document(collection = "Messages")
public class MessageModel {
    MessageType type;
    String sender;
    String content;
    Instant time;
    String reciver;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
