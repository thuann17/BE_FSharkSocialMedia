package com.system.fsharksocialmedia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {
    private String username;

    private String password;

    private String email;

    private Boolean active;

    private String gender;

    private String lastname;

    private String firstname;

    private String birthday;

    private String bio;

    private String hometown;

    private String currency;

    private Integer roleId;
}