package com.example.sky.controller.admin;

import com.example.sky.pojo.dto.DishAddDTO;
import com.example.sky.pojo.dto.DishPageQueryDTO;
import com.example.sky.pojo.dto.DishUpdateDTO;
import com.example.sky.pojo.entity.Dish;
import com.example.sky.pojo.vo.DishVO;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.service.DishService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageQueryVO> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageQueryVO pageQueryVO = dishService.page(dishPageQueryDTO);
        return Result.success(pageQueryVO);
    }

    /**
     * 编辑菜品状态
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result editStatus(@PathVariable Integer status, Long id) {
        log.info("编辑菜品状态，菜品id：{}，菜品状态：{}", id, status);
        dishService.editStatus(id, status);
        return Result.success();
    }

    /**
     * 获取菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> get(@PathVariable Long id) {
        log.info("获取菜品信息，菜品id：{}", id);
        DishVO dishVO = dishService.get(id);
        return Result.success(dishVO);
    }

    /**
     * 新增菜品
     *
     * @param dishAddDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody DishAddDTO dishAddDTO) {
        log.info("新增菜品：{}", dishAddDTO);
        dishService.add(dishAddDTO);
        return Result.success();
    }

    /**
     * 修改菜品信息
     *
     * @param dishUpdateDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody DishUpdateDTO dishUpdateDTO) {
        log.info("修改菜品信息：{}", dishUpdateDTO);
        dishService.update(dishUpdateDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result batchDelete(String ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 根据分类id获取菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("根据分类id获取菜品列表：{}", categoryId);
        List<Dish> dishList = dishService.listForAdmin(categoryId);
        return Result.success(dishList);
    }
}
