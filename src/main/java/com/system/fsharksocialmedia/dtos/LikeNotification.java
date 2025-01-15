package com.system.fsharksocialmedia.dtos;

import lombok.Data;


public class LikeNotification {
    private String username;
    private String message;

    public LikeNotification(String username, String message) {
        this.username = username;
        this.message = message;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
