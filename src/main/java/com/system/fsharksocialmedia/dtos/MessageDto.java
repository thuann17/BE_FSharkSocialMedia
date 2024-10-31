package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class MessageDto implements Serializable {
    Integer id;
    ConversationDto conversation;
    UserDto usersrc;
    @Size(max = 500)
    String content;
    Instant createdate;
}