package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class PostDto implements Serializable {
    Integer id;
    @Size(max = 200)
    String content;
    Instant createdate;
    Boolean status;
}