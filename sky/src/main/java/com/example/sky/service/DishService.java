package com.example.sky.service;

import com.example.sky.pojo.dto.DishAddDTO;
import com.example.sky.pojo.dto.DishPageQueryDTO;
import com.example.sky.pojo.dto.DishUpdateDTO;
import com.example.sky.pojo.entity.Dish;
import com.example.sky.pojo.vo.DishVO;
import com.example.sky.pojo.vo.PageQueryVO;

import java.util.List;

public interface DishService {
    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageQueryVO page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 编辑菜品状态
     * @param id
     * @param status
     */
    void editStatus(Long id, Integer status);

    /**
     * 获取菜品信息
     * @param id
     * @return
     */
    DishVO get(Long id);

    /**
     * 新增菜品
     * @param dishAddDTO
     */
    void add(DishAddDTO dishAddDTO);

    /**
     * 修改菜品信息
     * @param dishUpdateDTO
     */
    void update(DishUpdateDTO dishUpdateDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void batchDelete(String ids);

    /**
     * 根据分类id获取菜品列表：管理端
     * @param categoryId
     * @return
     */
    List<Dish> listForAdmin(Long categoryId);

    /**
     * 根据分类id获取菜品列表：用户端
     * @param categoryId
     * @return
     */
    List<DishVO> listForUser(Long categoryId);
}
