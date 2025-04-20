package com.example.sky.service.impl;

import com.example.sky.constant.CategoryStatusConstant;
import com.example.sky.constant.ExceptionTipConstant;
import com.example.sky.exception.CategoryNameDuplicateException;
import com.example.sky.exception.DishUnderCategoryException;
import com.example.sky.mapper.CategoryMapper;
import com.example.sky.mapper.DishMapper;
import com.example.sky.pojo.dto.CategoryAddDTO;
import com.example.sky.pojo.dto.CategoryPageQueryDTO;
import com.example.sky.pojo.dto.CategoryUpdateDTO;
import com.example.sky.pojo.entity.Category;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageQueryVO page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        // 执行查询操作
        Page<Category> result = categoryMapper.page(categoryPageQueryDTO);

        // 创建返回对象
        PageQueryVO pageQueryVO = new PageQueryVO();

        // 拷贝属性
        pageQueryVO.setTotal(result.getTotal());
        pageQueryVO.setRecords(result.getResult());

        return pageQueryVO;
    }

    /**
     * 新增分类
     *
     * @param categoryAddDTO
     */
    @Override
    public void add(CategoryAddDTO categoryAddDTO) {
        // 判断分类名称是否已存在
        Category category = categoryMapper.getByName(categoryAddDTO.getName());
        if (category != null)
            throw new CategoryNameDuplicateException(ExceptionTipConstant.CATEGORY_NAME_DUPLICATE);

        // 创建对象
        category = new Category();
        // 拷贝属性
        BeanUtils.copyProperties(categoryAddDTO, category);
        category.setStatus(CategoryStatusConstant.DISABLE); // 默认是禁用状态

        // 执行插入操作
        categoryMapper.add(category);
    }

    /**
     * 删除分类
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 只能删除没有关联菜品的分类
        if (hasDishUnderCategory(id))
            throw new DishUnderCategoryException(ExceptionTipConstant.DISH_UNDER_CATEGORY);
        else
            categoryMapper.delete(id);
    }

    /**
     * 编辑分类状态
     *
     * @param id
     * @param status
     */
    @Override
    public void editStatus(Long id, Integer status) {
        // 首先判断是禁用还是启用
        // 如果该分类下有菜品，则无法禁用
        if (status == CategoryStatusConstant.DISABLE && hasDishUnderCategory(id))
            throw new DishUnderCategoryException(ExceptionTipConstant.DISH_UNDER_CATEGORY);
        else {
            Category category = new Category();
            category.setId(id);
            category.setStatus(status);
            categoryMapper.editStatus(category);
        }
    }

    /**
     * 修改分类
     *
     * @param categoryUpdateDTO
     */
    @Override
    public void update(CategoryUpdateDTO categoryUpdateDTO) {
        // 判断分类名是否存在
        Category category = categoryMapper.getByNameExcludeId(categoryUpdateDTO.getName(), categoryUpdateDTO.getId());
        if (category != null)
            throw new CategoryNameDuplicateException(ExceptionTipConstant.CATEGORY_NAME_DUPLICATE);
        // 创建实体类对象
        category = new Category();
        // 拷贝属性
        BeanUtils.copyProperties(categoryUpdateDTO, category);
        // 执行更新操作
        categoryMapper.update(category);
    }

    /**
     * 获取分类列表
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> listForAdmin(Integer type) {
        return categoryMapper.listForAdmin(type);
    }

    /**
     * 获取分类列表
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> listForUser(Integer type) {
        if (type == null)
            return categoryMapper.listForUserWithoutType();
        return categoryMapper.listForUser(type);
    }

    /**
     * 判断分类下是否有菜品
     *
     * @param categoryId
     * @return
     */
    private boolean hasDishUnderCategory(Long categoryId) {
        return dishMapper.getCountByCategoryId(categoryId) == 0;
    }
}
