package com.system.fsharksocialmedia.models;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostModel {
    @Size(max = 200)
    String content;

}
