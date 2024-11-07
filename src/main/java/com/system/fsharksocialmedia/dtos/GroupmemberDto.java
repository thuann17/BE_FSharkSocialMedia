package com.system.fsharksocialmedia.dtos;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class GroupmemberDto implements Serializable {
    Integer id;
    ConversationDto conversation;
    UserDto username;
    Instant timejoin;
}