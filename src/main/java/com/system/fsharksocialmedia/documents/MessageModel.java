package com.system.fsharksocialmedia.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "messages")
public class MessageModel {
    private String sender;
    private String content;
    private Instant time;
    private String receiver;
//    private boolean isRead;
//    private boolean isDelivered;
//    private boolean isSeen;
    private boolean isDeletedBySender =false;
    private boolean isDeletedByReceiver =false;

}
