package com.example.sky.controller.user;

import com.example.sky.pojo.dto.ShoppingCartAddDTO;
import com.example.sky.pojo.dto.ShoppingCartSubDTO;
import com.example.sky.pojo.entity.ShoppingCart;
import com.example.sky.service.ShoppingCartService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 获取购物车列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result<ShoppingCart[]> list() {
        log.info("用户查看购物车列表");
        ShoppingCart[] shoppingCartList = shoppingCartService.list();
        return Result.success(shoppingCartList);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCartAddDTO
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartAddDTO shoppingCartAddDTO) {
        log.info("添加购物车：{}", shoppingCartAddDTO);
        shoppingCartService.add(shoppingCartAddDTO);
        return Result.success();
    }

    /**
     * 购物车数量减少
     *
     * @param shoppingCartSubDTO
     * @return
     */
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartSubDTO shoppingCartSubDTO) {
        log.info("购物车数量减一：{}", shoppingCartSubDTO);
        shoppingCartService.sub(shoppingCartSubDTO);
        return Result.success();
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public Result clean() {
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }
}
