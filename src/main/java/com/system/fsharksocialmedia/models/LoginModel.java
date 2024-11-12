package com.system.fsharksocialmedia.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LoginModel {
    String username;
    String password;
    String email;
    Integer roleId;
    Boolean active;
    Boolean gender;
    String lastname;
    String firstname;
    LocalDate birthday;
    String bio;
    String hometown;
    String currency;
}

