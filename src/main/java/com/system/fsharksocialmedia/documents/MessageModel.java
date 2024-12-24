package com.system.fsharksocialmedia.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "messages")  // MongoDB collection name
public class MessageModel {
    //    private MessageType type;
    private String sender;
    private String content;
    private Instant time;
    private String receiver;
    private boolean read;
//    public enum MessageType {
//        CHAT,
//        JOIN,
//        LEAVE
//    }
}
