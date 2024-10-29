package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class CommentDto implements Serializable {
    Integer id;
    @Size(max = 500)
    String content;
    UserDto username;
    Instant createdate;
    PostDto post;
    String image;
}