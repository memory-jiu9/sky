package com.example.sky.exception;

public class EmployeeNotFoundException extends BaseException {
    public EmployeeNotFoundException() {
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
