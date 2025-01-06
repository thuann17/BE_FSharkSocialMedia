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
    private String parentMessageId;
    String urlImage;
    private boolean isStatus = true;
    private boolean isDeletedBySender = false;
    private boolean isDeletedByReceiver = false;

}
