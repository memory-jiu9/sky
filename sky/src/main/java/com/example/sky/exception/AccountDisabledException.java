package com.example.sky.exception;

public class AccountDisabledException extends BaseException {
    public AccountDisabledException() {
    }

    public AccountDisabledException(String message) {
        super(message);
    }
}
