package com.system.fsharksocialmedia.models;

import lombok.Data;

import java.time.Instant;

@Data
public class InteractModel {
    private String username;
    private Integer postID;
    private Instant createDate;
    private String image;
    private String content;
}
