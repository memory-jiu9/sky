package com.example.sky.service;

import com.example.sky.pojo.dto.SetmealAddDTO;
import com.example.sky.pojo.dto.SetmealPageQueryDTO;
import com.example.sky.pojo.dto.SetmealUpdateDTO;
import com.example.sky.pojo.entity.Setmeal;
import com.example.sky.pojo.entity.SetmealDish;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.pojo.vo.SetmealDishVO;
import com.example.sky.pojo.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageQueryVO page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealAddDTO
     */
    void add(SetmealAddDTO setmealAddDTO);

    /**
     * 修改套餐
     * @param setmealUpdateDTO
     */
    void update(SetmealUpdateDTO setmealUpdateDTO);

    /**
     * 获取套餐信息
     * @param id
     * @return
     */
    SetmealVO get(Long id);

    /**
     * 修改套餐状态
     * @param id
     * @param status
     */
    void editStatus(Long id, Integer status);

    /**
     * 批量删除套餐
     * @param ids
     */
    void batchDelete(String ids);

    /**
     * 用户根据分类查询套餐
     * @param categoryId
     * @return
     */
    List<Setmeal> list(Long categoryId);

    /**
     * 用户查看该套餐下的菜品
     * @param id
     * @return
     */
    SetmealDishVO[] getDish(Long id);
}
