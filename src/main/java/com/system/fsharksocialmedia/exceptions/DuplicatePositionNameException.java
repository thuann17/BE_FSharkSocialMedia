package com.system.fsharksocialmedia.exceptions;

public class DuplicatePositionNameException extends RuntimeException {
    public DuplicatePositionNameException(String positionName) {
        super("Chức vụ '" + positionName + "' đã tồn tại.");
    }
}