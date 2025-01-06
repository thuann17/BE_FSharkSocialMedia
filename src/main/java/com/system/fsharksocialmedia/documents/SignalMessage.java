package com.system.fsharksocialmedia.documents;

import lombok.Data;

@Data
public class SignalMessage {
    private String type;
    private String from;
    private String to;
    private String sdp;
    private String candidate;
}
