package com.system.fsharksocialmedia.documents;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class MessageDto implements Serializable {
    private String sender;
    private String receiver;
    private String content;
    private Instant time;
}
