package com.example.sky.service.impl;

import com.example.sky.constant.ShopStatusConstant;
import com.example.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取店铺营业状态
     *
     * @return
     */
    @Override
    public Integer get() {
        // 获取操作String的对象
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

        // 获取状态
        String status = opsForValue.get(ShopStatusConstant.SHOP_STATUS);
        if (status == null)
            return ShopStatusConstant.REST;
        else
            return Integer.valueOf(status);
    }

    /**
     * 设置店铺营业状态
     *
     * @param status
     */
    @Override
    public void set(Integer status) {
        // 获取操作String的对象
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

        // 设置营业状态
        opsForValue.set(ShopStatusConstant.SHOP_STATUS, String.valueOf(status));
    }
}
