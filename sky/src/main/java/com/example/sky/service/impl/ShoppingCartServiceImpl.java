package com.example.sky.service.impl;

import com.example.sky.context.BaseContext;
import com.example.sky.mapper.DishMapper;
import com.example.sky.mapper.SetmealMapper;
import com.example.sky.mapper.ShoppingCartMapper;
import com.example.sky.pojo.dto.ShoppingCartAddDTO;
import com.example.sky.pojo.dto.ShoppingCartSubDTO;
import com.example.sky.pojo.entity.Dish;
import com.example.sky.pojo.entity.Setmeal;
import com.example.sky.pojo.entity.ShoppingCart;
import com.example.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 用户查看购物车列表
     *
     * @return
     */
    @Override
    public ShoppingCart[] list() {
        // 获取用户id
        Long userId = BaseContext.getUserId();

        // 获取购物车列表
        return shoppingCartMapper.list(userId);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCartAddDTO
     */
    @Override
    public void add(ShoppingCartAddDTO shoppingCartAddDTO) {
        // 获取用户id
        Long userId = BaseContext.getUserId();

        // 判断用户选择的是套餐还是单道菜品
        if (shoppingCartAddDTO.getSetmealId() == null) {
            // 单道菜品，判断该菜品是否已经存在
            ShoppingCart shoppingCart = shoppingCartMapper.getByDishId(shoppingCartAddDTO.getDishId(), userId);

            // 获取dish记录
            Dish dish = dishMapper.getById(shoppingCartAddDTO.getDishId());

            // 数量+1，更新价格
            if (shoppingCart != null) {

                Integer number = shoppingCart.getNumber();
                number++;
                BigDecimal amount = calculateAmount(number, dish.getPrice());

                shoppingCartMapper.updateNumber(number, amount, shoppingCart.getId());

            } else {
                // 创建ShoppingCart实例
                shoppingCart = new ShoppingCart();

                // 拷贝属性
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setUserId(userId);
                shoppingCart.setDishId(dish.getId());
                shoppingCart.setDishFlavor(shoppingCartAddDTO.getDishFlavor());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setCreateTime(LocalDateTime.now());

                // 执行插入操作
                shoppingCartMapper.add(shoppingCart);
            }
        } else {
            // 套餐，判断套餐是否存在
            ShoppingCart shoppingCart = shoppingCartMapper.getBySetmealId(shoppingCartAddDTO.getSetmealId(), userId);

            Setmeal setmeal = setmealMapper.getById(shoppingCartAddDTO.getSetmealId());

            if (shoppingCart != null) {
                Integer number = shoppingCart.getNumber();
                number++;
                BigDecimal amount = calculateAmount(number, setmeal.getPrice());

                shoppingCartMapper.updateNumber(number, amount, shoppingCart.getId());
            } else {
                // 创建ShoppingCart实例
                shoppingCart = new ShoppingCart();

                // 拷贝属性
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setUserId(userId);
                shoppingCart.setSetmealId(setmeal.getId());
                shoppingCart.setDishFlavor(shoppingCartAddDTO.getDishFlavor());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setCreateTime(LocalDateTime.now());

                // 执行插入操作
                shoppingCartMapper.add(shoppingCart);
            }
        }
    }

    /**
     * 购物车数量减一
     *
     * @param shoppingCartSubDTO
     */
    @Override
    public void sub(ShoppingCartSubDTO shoppingCartSubDTO) {
        // 获取用户id
        Long userId = BaseContext.getUserId();
        // 判断用户选择的是套餐还是单道菜品
        if (shoppingCartSubDTO.getSetmealId() == null) {
            ShoppingCart shoppingCart = shoppingCartMapper.getByDishId(shoppingCartSubDTO.getDishId(), userId);
            // 获取dish记录
            Dish dish = dishMapper.getById(shoppingCartSubDTO.getDishId());

            // 获取数量
            Integer number = shoppingCart.getNumber();
            number--;
            if (number == 0) {
                // 删除
                shoppingCartMapper.delete(shoppingCart.getId());
            } else {
                // 计算价格
                BigDecimal amount = calculateAmount(number, dish.getPrice());
                // 更新
                shoppingCartMapper.updateNumber(number, amount, shoppingCart.getId());
            }
        } else {
            ShoppingCart shoppingCart = shoppingCartMapper.getBySetmealId(shoppingCartSubDTO.getSetmealId(), userId);

            Setmeal setmeal = setmealMapper.getById(shoppingCartSubDTO.getSetmealId());

            // 获取数量
            Integer number = shoppingCart.getNumber();
            number--;
            if (number == 0) {
                // 删除
                shoppingCartMapper.delete(shoppingCart.getId());
            } else {
                // 计算价格
                BigDecimal amount = calculateAmount(number, setmeal.getPrice());
                // 更新
                shoppingCartMapper.updateNumber(number, amount, shoppingCart.getId());
            }
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        // 获取用户id
        Long userId = BaseContext.getUserId();

        // 执行删除操作
        shoppingCartMapper.clean(userId);
    }

    // 计算价格
    public BigDecimal calculateAmount(Integer number, BigDecimal price) {
        BigDecimal sum = BigDecimal.valueOf(number);
        BigDecimal amount = sum.multiply(price);
        return amount;
    }
}
