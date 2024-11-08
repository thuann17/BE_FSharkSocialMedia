package com.system.fsharksocialmedia.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageDto {

    private Integer id;

    private String image;

    private LocalDateTime createDate;

    private String avatarUrl;

    private String coverUrl;

    private Boolean status;

    private String username; // Username instead of User object for simplicity

}
