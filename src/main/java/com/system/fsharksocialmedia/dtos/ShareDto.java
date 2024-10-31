package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class ShareDto implements Serializable {
    Integer id;
    UserDto username;
    PostDto post;
    @Size(max = 500)
    String content;
    Instant createdate;
}