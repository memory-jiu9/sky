package com.example.sky.mapper;

import com.example.sky.pojo.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据userId获取购物车列表
     *
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{userId}")
    ShoppingCart[] list(Long userId);

    /**
     * 更新购物车数量，总价格
     *
     * @param number
     * @param amount
     * @param id
     */
    @Update("update shopping_cart set number = #{number}, amount = #{amount} where id = #{id}")
    void updateNumber(Integer number, BigDecimal amount, Long id);

    /**
     * 判断用户的购物车中是否已经有该菜品
     *
     * @param dishId
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{userId} and dish_id = #{dishId}")
    ShoppingCart getByDishId(Long dishId, Long userId);

    /**
     * 添加购物车
     *
     * @param shoppingCart
     */
    void add(ShoppingCart shoppingCart);

    /**
     * 判断用户的购物车中是否已经有该菜品
     *
     * @param setmealId
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{userId} and setmeal_id = #{setmealId}")
    ShoppingCart getBySetmealId(Long setmealId, Long userId);

    /**
     * 根据id删除购物车
     * @param id
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void delete(Long id);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(Long userId);
}
