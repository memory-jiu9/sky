package com.example.sky.interceptor;

import com.example.sky.constant.BaseContextConstant;
import com.example.sky.constant.ShopStatusConstant;
import com.example.sky.context.BaseContext;
import com.example.sky.properties.JwtProperties;
import com.example.sky.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
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
            // 获取id
            Long id = claims.get(BaseContextConstant.ID, Long.class);

            BaseContext.setUserId(id);
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

        // 判断店铺是否在营业
        String status = stringRedisTemplate.opsForValue().get(ShopStatusConstant.SHOP_STATUS);
        if (ShopStatusConstant.REST.equals(Integer.valueOf(status)))
            // 如果店铺是打烊状态，则请求失败
            return false;

        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除ThreadLocal的数据
        BaseContext.removeUserId();
    }
}
