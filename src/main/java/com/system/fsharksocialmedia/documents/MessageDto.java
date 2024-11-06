package com.system.fsharksocialmedia.documents;

import com.system.fsharksocialmedia.dtos.UserDto;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.time.Instant;

@Data
public class MessageDto implements Serializable {
    private String sender;
    private String reciver;
    private String content;
    private Instant timestamp;
}