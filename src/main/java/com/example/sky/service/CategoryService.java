package com.example.sky.service;

import com.example.sky.pojo.dto.CategoryAddDTO;
import com.example.sky.pojo.dto.CategoryPageQueryDTO;
import com.example.sky.pojo.dto.CategoryUpdateDTO;
import com.example.sky.pojo.entity.Category;
import com.example.sky.pojo.vo.PageQueryVO;

import java.util.List;

public interface CategoryService {
    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageQueryVO page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param categoryAddDTO
     */
    void add(CategoryAddDTO categoryAddDTO);

    /**
     * 删除分类
     * @param id
     */
    void delete(Long id);

    /**
     * 编辑分类状态
     * @param id
     * @param status
     */
    void editStatus(Long id, Integer status);

    /**
     * 修改分类
     * @param categoryUpdateDTO
     */
    void update(CategoryUpdateDTO categoryUpdateDTO);

    /**
     * 获取分类列表
     * @param type
     * @return
     */
    List<Category> listForAdmin(Integer type);

    /**
     * 获取分类列表
     * @param type
     * @return
     */
    List<Category> listForUser(Integer type);
}
