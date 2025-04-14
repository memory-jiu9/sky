package com.example.sky.interceptor;

import com.example.sky.context.BaseContext;
import com.example.sky.properties.JwtProperties;
import com.example.sky.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 获取token
        String token = request.getHeader(jwtProperties.getUserTokenName());
        // token不存在，statusCode = 401，未认证状态
        if (token == null) {
            response.setStatus(401);
            return false;
        }
        // 验证token
        Claims claims = null;

        try {
            claims = JwtUtil.parseToken(jwtProperties.getUserSecretKey(), token);
            // TODO claims.get(); toknen里面放什么东西？
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除ThreadLocal的数据
        BaseContext.remove();
    }
}
