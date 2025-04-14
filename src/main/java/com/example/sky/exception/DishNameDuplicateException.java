package com.example.sky.exception;

public class DishNameDuplicateException extends BaseException {
    public DishNameDuplicateException() {
    }

    public DishNameDuplicateException(String message) {
        super(message);
    }
}
