package com.example.sky.service;

import com.example.sky.pojo.dto.ShoppingCartAddDTO;
import com.example.sky.pojo.dto.ShoppingCartSubDTO;
import com.example.sky.pojo.entity.ShoppingCart;

public interface ShoppingCartService {
    /**
     * 用户查看购物车列表
     * @return
     */
    ShoppingCart[] list();

    /**
     * 添加购物车
     * @param shoppingCartAddDTO
     */
    void add(ShoppingCartAddDTO shoppingCartAddDTO);

    /**
     * 购物车数量减一
     * @param shoppingCartSubDTO
     */
    void sub(ShoppingCartSubDTO shoppingCartSubDTO);

    /**
     * 清空购物车
     */
    void clean();
}
