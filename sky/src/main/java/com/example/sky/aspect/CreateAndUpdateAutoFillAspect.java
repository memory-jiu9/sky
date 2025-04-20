package com.example.sky.aspect;

import com.example.sky.annotation.CreateAndUpdateAutoFill;
import com.example.sky.constant.CreateAndUpdateAutoFillConstant;
import com.example.sky.constant.ExceptionTipConstant;
import com.example.sky.constant.OperationTypeConstant;
import com.example.sky.context.BaseContext;
import com.example.sky.exception.UnknownInputException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class CreateAndUpdateAutoFillAspect {

    /**
     * 前置增强
     *
     * @param joinPoint
     */
    @Before("execution(* com.example.sky.mapper.*.*(..)) && @annotation(com.example.sky.annotation.CreateAndUpdateAutoFill)")
    public void autoFill(JoinPoint joinPoint) {
        log.info("切面编程触发");
        // 获取方法签名：方法名 + 参数列表
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 获取方法，根据得到的method实例获取注解
        CreateAndUpdateAutoFill createAndUpdateAutoFill = methodSignature.getMethod().getAnnotation(CreateAndUpdateAutoFill.class);
        // 从而获取注解传入的操作类型
        int operationType = createAndUpdateAutoFill.operationType();

        // 获取实参
        Object entity = joinPoint.getArgs()[0];
        // 反射获取Class对象
        Class entityClass = entity.getClass();

        // 准备数据
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getEmpId();

        // 根据操作类型进行增强
        if (operationType == OperationTypeConstant.INSERT) {
            // 反射赋值
            try {
                Method setCreateTime = entityClass.getDeclaredMethod(CreateAndUpdateAutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                setCreateTime.setAccessible(true);
                setCreateTime.invoke(entity, LocalDateTime.now());

                Method setUpdateTime = entityClass.getDeclaredMethod(CreateAndUpdateAutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                setUpdateTime.setAccessible(true);
                setUpdateTime.invoke(entity, LocalDateTime.now());

                Method setCreateUser = entityClass.getDeclaredMethod(CreateAndUpdateAutoFillConstant.SET_CREATE_USER, Long.class);
                setCreateUser.setAccessible(true);
                setCreateUser.invoke(entity, id);

                Method setUpdateUser = entityClass.getDeclaredMethod(CreateAndUpdateAutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateUser.setAccessible(true);
                setUpdateUser.invoke(entity, id);

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        } else if (operationType == OperationTypeConstant.UPDATE) {
            try {
                Method setUpdateTime = entityClass.getDeclaredMethod(CreateAndUpdateAutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                setUpdateTime.setAccessible(true);
                setUpdateTime.invoke(entity, LocalDateTime.now());

                Method setUpdateUser = entityClass.getDeclaredMethod(CreateAndUpdateAutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateUser.setAccessible(true);
                setUpdateUser.invoke(entity, id);

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        } else
            throw new UnknownInputException(ExceptionTipConstant.UNKNOWN_INPUT);
    }
}
