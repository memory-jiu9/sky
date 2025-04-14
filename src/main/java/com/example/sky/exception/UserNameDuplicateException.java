package com.example.sky.exception;

public class UserNameDuplicateException extends BaseException {
    public UserNameDuplicateException() {
    }

    public UserNameDuplicateException(String message) {
        super(message);
    }
}
