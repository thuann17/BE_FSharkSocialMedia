package com.system.fsharksocialmedia.models;

import com.system.fsharksocialmedia.dtos.UserDto;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Data
public class PostModel {
    @Size(max = 200)
    String content;
    String username;
    boolean status;
    List<String> imagePosts;
}
