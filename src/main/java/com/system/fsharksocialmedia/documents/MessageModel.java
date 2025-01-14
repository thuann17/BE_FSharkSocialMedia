package com.system.fsharksocialmedia.documents;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "messages")
public class MessageModel {
    private ObjectId id;
    private String sender;
    private String content;
    private Instant time;
    private String receiver;
    private String parentMessageId;
    private boolean isReal = false;
    private String urlImage;
    private String type;
    private boolean isDeletedBySender = false;
    private boolean isDeletedByReceiver = false;

}
