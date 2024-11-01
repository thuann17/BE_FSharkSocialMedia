package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto implements Serializable {
    @Size(max = 200)
    String username;
    UserroleDto roles;
    @Size(max = 255)
    String password;
    Boolean active;
    @Size(max = 200)
    String email;
    Boolean gender;
    @Size(max = 100)
    String lastname;
    @Size(max = 100)
    String firstname;
    LocalDate birthday;
    @Size(max = 500)
    String bio;
    @Size(max = 200)
    String hometown;
    @Size(max = 100)
    String currency;
    List<ImageDto> images;
}