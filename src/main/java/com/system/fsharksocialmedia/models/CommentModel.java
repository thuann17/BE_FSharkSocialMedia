package com.system.fsharksocialmedia.models;

import com.system.fsharksocialmedia.entities.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CommentModel {
    @NotBlank(message = "Vui lòng nhập...")
    @Size(min = 1, message = "Vui lòng nhập...")
    String content;
    int postID;
    String image;
}
