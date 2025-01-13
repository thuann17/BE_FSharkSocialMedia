package com.system.fsharksocialmedia.models;

import lombok.Data;

import java.time.Instant;

@Data
public class ShareModel {

    private String content;
    private Instant createdate = Instant.now();
}
