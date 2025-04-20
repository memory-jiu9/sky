package com.example.sky.controller.user;

import com.example.sky.pojo.entity.Dish;
import com.example.sky.pojo.vo.DishVO;
import com.example.sky.service.DishService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("用户根据分类id查询菜品：{}", categoryId);
        List<DishVO> dishList = dishService.listForUser(categoryId);
        return Result.success(dishList);
    }
}
