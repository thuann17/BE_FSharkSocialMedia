package com.system.fsharksocialmedia.models;

import com.system.fsharksocialmedia.dtos.UserroleDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserModel {
    String username;
    String roles;
    String password;
    Boolean active;
    String email;
    Boolean gender;
    String lastname;
    String firstname;
    LocalDate birthday;
    String bio;
    String hometown;
    String currency;
    String coverUrl;
    String avatarUrl;
    String message;
}
