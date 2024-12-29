package com.system.fsharksocialmedia.models;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserModel {
    @NotBlank(message = "Tên người dùng không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 50, message = "Mật khẩu có độ dài từ 6 đến 50 ký tự")
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Địa chỉ email không hợp lệ")
    private String email;

    private Integer roleId;
    private Boolean active;

    @AssertTrue(message = "Giới tính không hợp lệ")
    private Boolean gender;

    @NotBlank(message = "Tên không được để trống")
    private String firstname;

    @NotBlank(message = "Họ không được để trống")
    private String lastname;

    private LocalDate birthday;
    private String bio;
    private String hometown;
    private String currency;
    private String avatarUrl;
    private String token;
}
