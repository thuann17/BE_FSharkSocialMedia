package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class ConversationDto implements Serializable {
    Integer id;
    @Size(max = 300)
    String name;
    Instant createdat;
    String avatar;
}