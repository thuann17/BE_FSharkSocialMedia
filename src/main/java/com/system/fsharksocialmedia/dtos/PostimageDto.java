package com.system.fsharksocialmedia.dtos;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class PostimageDto implements Serializable {
    Integer id;
    PostDto postid;
    String image;
}