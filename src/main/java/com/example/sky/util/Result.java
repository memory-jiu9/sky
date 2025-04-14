package com.example.sky.util;

import com.example.sky.constant.ResultCodeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<E> {
    private Integer code;
    private String msg;
    private E data;

    public static Result success() {
        return new Result(ResultCodeConstant.SUCCESS, null, null);
    }

    public static <E> Result success(E data) {
        return new Result(ResultCodeConstant.SUCCESS, null, data);
    }

    public static Result error(String msg) {
        return new Result(ResultCodeConstant.ERROR, msg, null);
    }
}
