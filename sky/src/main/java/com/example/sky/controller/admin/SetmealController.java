package com.example.sky.controller.admin;

import com.example.sky.pojo.dto.SetmealAddDTO;
import com.example.sky.pojo.dto.SetmealPageQueryDTO;
import com.example.sky.pojo.dto.SetmealUpdateDTO;
import com.example.sky.pojo.entity.Setmeal;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.pojo.vo.SetmealPageQueryVO;
import com.example.sky.pojo.vo.SetmealVO;
import com.example.sky.service.SetmealService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageQueryVO> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}", setmealPageQueryDTO);
        PageQueryVO pageQueryVO = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageQueryVO);
    }

    /**
     * 新增套餐
     *
     * @param setmealAddDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody SetmealAddDTO setmealAddDTO) {
        log.info("新增套餐：{}", setmealAddDTO);
        setmealService.add(setmealAddDTO);
        return Result.success();
    }

    /**
     * 修改套餐
     *
     * @param setmealUpdateDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody SetmealUpdateDTO setmealUpdateDTO) {
        log.info("修改套餐：{}", setmealUpdateDTO);
        setmealService.update(setmealUpdateDTO);
        return Result.success();
    }

    /**
     * 获取套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> get(@PathVariable Long id) {
        log.info("获取套餐信息：{}", id);
        SetmealVO setmealVO = setmealService.get(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐状态
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result editStatus(@PathVariable Integer status, Long id) {
        log.info("修改套餐状态，套餐id：{}，状态：{}", id, status);
        setmealService.editStatus(id, status);
        return Result.success();
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result batchDelete(String ids) {
        log.info("批量删除套餐：{}", ids);
        setmealService.batchDelete(ids);
        return Result.success();
    }
}
