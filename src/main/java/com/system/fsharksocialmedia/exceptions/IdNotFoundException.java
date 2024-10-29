package com.system.fsharksocialmedia.exceptions;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(Integer id) {
        super("Không tìm thấy mã " + id + " trong hệ thống."); // Thông báo lỗi
    }
}