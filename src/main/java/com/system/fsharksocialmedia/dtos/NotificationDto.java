package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class NotificationDto implements Serializable {
    Integer id;
    String username;
    @Size(max = 500)
    String content;
    TypeDto type;
    PostDto post;
    Boolean status;
    Instant createdate;
}