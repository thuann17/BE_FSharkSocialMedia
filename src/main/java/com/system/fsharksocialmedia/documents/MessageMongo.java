package com.system.fsharksocialmedia.documents;

import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
@Data
public class MessageMongo {
    @Id
    private ObjectId id;
    private String sender;
    private String reciver;
    private String content;
    private Instant timestamp;
}
