package com.example.sky.controller.user;

import com.example.sky.pojo.entity.Setmeal;
import com.example.sky.pojo.entity.SetmealDish;
import com.example.sky.pojo.vo.SetmealDishVO;
import com.example.sky.pojo.vo.SetmealVO;
import com.example.sky.service.SetmealService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 用户根据分类查询套餐
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId) {
        log.info("用户根据分类查询套餐：{}", categoryId);
        List<Setmeal> setmealList = setmealService.list(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 用户查看该套餐下的菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<SetmealDishVO[]> getDish(@PathVariable Long id) {
        log.info("用户查看该套餐下的菜品，套餐id：{}", id);
        SetmealDishVO[] setmealDishVOList = setmealService.getDish(id);
        return Result.success(setmealDishVOList);
    }
}
