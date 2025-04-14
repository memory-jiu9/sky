package com.example.sky.context;

import org.springframework.stereotype.Component;

@Component
public class BaseContext {
    private static ThreadLocal<Long> id = new ThreadLocal<>();

    public static Long get() {
        return id.get();
    }

    public static void set(Long id) {
        BaseContext.id.set(id);
    }

    public static void remove() {
        id.remove();
    }
}
