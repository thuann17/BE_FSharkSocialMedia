package com.system.fsharksocialmedia.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}
