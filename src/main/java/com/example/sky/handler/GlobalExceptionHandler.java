package com.example.sky.handler;

import com.example.sky.exception.BaseException;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handlerException(BaseException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
