package com.example.sky.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.sky.constant.DishStatusConstant;
import com.example.sky.constant.ExceptionTipConstant;
import com.example.sky.exception.DishNameDuplicateException;
import com.example.sky.exception.DishUnderSetmealException;
import com.example.sky.mapper.*;
import com.example.sky.pojo.dto.DishAddDTO;
import com.example.sky.pojo.dto.DishPageQueryDTO;
import com.example.sky.pojo.dto.DishUpdateDTO;
import com.example.sky.pojo.entity.Dish;
import com.example.sky.pojo.entity.DishFlavor;
import com.example.sky.pojo.entity.SetmealDish;
import com.example.sky.pojo.vo.DishPageQueryVO;
import com.example.sky.pojo.vo.DishVO;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.service.DishService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageQueryVO page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        // 执行查询操作
        Page<DishPageQueryVO> result = dishMapper.page(dishPageQueryDTO);

        // 创建返回对象
        PageQueryVO pageQueryVO = new PageQueryVO();

        // 拷贝属性
        pageQueryVO.setRecords(result.getResult());
        pageQueryVO.setTotal(result.getTotal());

        return pageQueryVO;
    }

    /**
     * 编辑菜品状态
     *
     * @param id
     * @param status
     */
    @Override
    public void editStatus(Long id, Integer status) {
        // 判断是不是禁用操作
        if (status == DishStatusConstant.DISABLE) {
            // 判断该菜品是否被加入到套餐中，在setmeal_dish中查询
            Long setmealId = setmealDishMapper.getByDishId(id);

            if (setmealId != null) {
                // 获取套餐名字
                String name = setmealMapper.getNameById(setmealId);
                throw new DishUnderSetmealException(ExceptionTipConstant.DISH_UNDER_SETMEAL + name);
            }
        }
        // 创建Dish实例对象
        Dish dish = new Dish();

        // 拷贝属性
        dish.setId(id);
        dish.setStatus(status);

        dishMapper.editStatus(dish);

        // 从redis中删除副本
        stringRedisTemplate.delete("category-" + dishMapper.getById(id).getCategoryId());
    }

    /**
     * 获取菜品信息
     *
     * @param id
     * @return
     */
    @Override
    public DishVO get(Long id) {
        // 先获取菜品
        Dish dish = dishMapper.getById(id);

        // 根据菜品分类id获取分类名
        String categoryName = categoryMapper.getNameById(dish.getCategoryId());

        // 根据菜品id获取口味列表
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dish.getId());

        // 创建返回对象
        DishVO dishVO = new DishVO();

        // 拷贝属性
        dishVO.setFlavors(flavors);
        dishVO.setCategoryName(categoryName);
        BeanUtils.copyProperties(dish, dishVO);

        return dishVO;
    }

    /**
     * 新增菜品
     *
     * @param dishAddDTO
     */
    @Override
    @Transactional
    public void add(DishAddDTO dishAddDTO) {
        // 判断菜品名称是否重复
        Dish dish = dishMapper.getByName(dishAddDTO.getName());
        if (dish != null)
            throw new DishNameDuplicateException(ExceptionTipConstant.DISH_NAME_DUPLICATE);

        // 创建Dish对象
        dish = new Dish();

        // 拷贝属性
        BeanUtils.copyProperties(dishAddDTO, dish);
        dish.setStatus(DishStatusConstant.DISABLE); // 默认禁用

        // 对dish表执行插入操作
        dishMapper.add(dish);

        // 对dish_flavor表执行插入操作
        if (dishAddDTO.getFlavors() != null && dishAddDTO.getFlavors().length != 0)
            dishFlavorMapper.batchAdd(dishAddDTO.getFlavors(), dish.getId());

        // 从redis中删除副本
        stringRedisTemplate.delete("category-" + dish.getCategoryId());
    }

    /**
     * 修改菜品信息
     *
     * @param dishUpdateDTO
     */
    @Override
    @Transactional
    public void update(DishUpdateDTO dishUpdateDTO) {
        // 判断菜品名称是否重复
        Dish dish = dishMapper.getByNameExcludeId(dishUpdateDTO.getName(), dishUpdateDTO.getId());
        if (dish != null)
            throw new DishNameDuplicateException(ExceptionTipConstant.DISH_NAME_DUPLICATE);

        // 创建Dish对象
        dish = new Dish();

        // 拷贝属性
        BeanUtils.copyProperties(dishUpdateDTO, dish);

        // 对dish表执行插入操作
        dishMapper.update(dish);

        // 先将dish_flavor中该菜品的口味列表删除
        dishFlavorMapper.batchDeleteById(dish.getId());

        // 再对dish_flavor表执行插入操作
        if (dishUpdateDTO.getFlavors() != null && dishUpdateDTO.getFlavors().length != 0)
            dishFlavorMapper.batchAdd(dishUpdateDTO.getFlavors(), dish.getId());

        // 从redis中删除副本
        stringRedisTemplate.delete("category-" + dish.getCategoryId());
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Override
    @Transactional
    public void batchDelete(String ids) {

        // 获取id列表
        String[] idList = ids.split(",");

        // 从redis中删除副本
        List<Long> categoryIdList = dishMapper.getCategoryId(idList);
        Set<String> categoryIdSet = categoryIdList.stream()
                .distinct()
                .map(id -> "category-" + id)
                .collect(Collectors.toSet());
        stringRedisTemplate.delete(categoryIdSet);

        // 批量删除菜品
        dishMapper.batchDelete(idList);
        // 批量删除该菜品的口味列表
        dishFlavorMapper.batchDeleteByIdList(idList);

    }

    /**
     * 根据分类id获取菜品列表：管理端
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> listForAdmin(Long categoryId) {
        return dishMapper.list(categoryId);
    }

    /**
     * 根据分类id获取菜品列表：服务端
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> listForUser(Long categoryId) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

        // 判断是否在 redis 中有副本
        String json = opsForValue.get("category-" + categoryId);

        if (json == null) {
            // 没有副本
            // 获取菜品列表
            List<Dish> dishList = dishMapper.list(categoryId);

            // 创建菜品视图类列表
            List<DishVO> dishVOList = new ArrayList<>();

            // 遍历菜品，获取该菜品的口味数据，然后加入到dishVOList中
            for (Dish dish : dishList) {
                List<DishFlavor> flavorList = dishFlavorMapper.getByDishId(dish.getId());
                DishVO dishVO = new DishVO();
                dishVO.setFlavors(flavorList);
                BeanUtils.copyProperties(dish, dishVO);
                dishVOList.add(dishVO);
            }

            // 加入redis
            opsForValue.set("category-" + categoryId, JSON.toJSONString(dishVOList));
            return dishVOList;
        } else {
            // 解析json
            System.out.println("菜品redis触发");
            List list = JSON.parseObject(json, List.class);
            return list;
        }
    }
}
