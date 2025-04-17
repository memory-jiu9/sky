package com.example.sky.service;

public interface ShopService {
    /**
     * 获取店铺营业状态
     * @return
     */
    Integer get();

    /**
     * 设置店铺营业状态
     * @param status
     */
    void set(Integer status);
}
