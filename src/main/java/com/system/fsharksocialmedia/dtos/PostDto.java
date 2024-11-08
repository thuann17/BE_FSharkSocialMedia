package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PostDto implements Serializable {
    Integer id;

    UserDto username;

    @Size(max = 200)
    String content;

    Instant createdate;

    Boolean status;

    Integer commentCount;

    Integer likeCount;

    List<String> images;
}