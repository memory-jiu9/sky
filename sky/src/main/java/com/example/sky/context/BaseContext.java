package com.example.sky.context;

import org.springframework.stereotype.Component;

@Component
public class BaseContext {
    private static ThreadLocal<Long> EmpId = new ThreadLocal<>();
    private static ThreadLocal<Long> UserId = new ThreadLocal<>();

    public static Long getEmpId() {
        return EmpId.get();
    }

    public static void setEmpId(Long empId) {
        BaseContext.EmpId.set(empId);
    }

    public static void removeEmpId() {
        EmpId.remove();
    }

    public static Long getUserId() {
        return UserId.get();
    }

    public static void setUserId(Long userId) {
        BaseContext.UserId.set(userId);
    }

    public static void removeUserId() {
        UserId.remove();
    }
}
