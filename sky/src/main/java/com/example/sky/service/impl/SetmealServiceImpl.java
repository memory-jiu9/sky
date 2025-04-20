package com.example.sky.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.sky.constant.ExceptionTipConstant;
import com.example.sky.constant.SetmealStatusConstant;
import com.example.sky.exception.SetmealNameDuplicateException;
import com.example.sky.mapper.SetmealDishMapper;
import com.example.sky.mapper.SetmealMapper;
import com.example.sky.pojo.dto.SetmealAddDTO;
import com.example.sky.pojo.dto.SetmealPageQueryDTO;
import com.example.sky.pojo.dto.SetmealUpdateDTO;
import com.example.sky.pojo.entity.Setmeal;
import com.example.sky.pojo.entity.SetmealDish;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.pojo.vo.SetmealDishVO;
import com.example.sky.pojo.vo.SetmealPageQueryVO;
import com.example.sky.pojo.vo.SetmealVO;
import com.example.sky.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageQueryVO page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        // 执行查询操作
        Page<SetmealPageQueryVO> result = setmealMapper.page(setmealPageQueryDTO);

        // 创建返回对象
        PageQueryVO pageQueryVO = new PageQueryVO();

        // 拷贝属性
        pageQueryVO.setTotal(result.getTotal());
        pageQueryVO.setRecords(result.getResult());

        return pageQueryVO;
    }

    /**
     * 新增套餐
     *
     * @param setmealAddDTO
     */
    @Override
    @Transactional
    public void add(SetmealAddDTO setmealAddDTO) {
        // 判断套餐名是否存在
        Setmeal setmeal = setmealMapper.getByName(setmealAddDTO.getName());
        if (setmeal != null)
            throw new SetmealNameDuplicateException(ExceptionTipConstant.SETMEAL_NAME_DUPLICATE);

        // 向setmeal表中插入
        setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealAddDTO, setmeal);
        setmeal.setStatus(SetmealStatusConstant.DISABLE); // 默认禁用

        setmealMapper.add(setmeal);

        // 向setmeal_dish表中插入
        Object[] setmealDishList = setmealAddDTO.getSetmealDishes();
        setmealDishMapper.batchAdd(setmealDishList, setmeal.getId());

        // 从redis中删除副本
        stringRedisTemplate.delete("category-" + setmeal.getCategoryId());
    }

    /**
     * 修改套餐
     *
     * @param setmealUpdateDTO
     */
    @Override
    @Transactional
    public void update(SetmealUpdateDTO setmealUpdateDTO) {
        // 判断套餐名是否存在
        Setmeal setmeal = setmealMapper.getByNameExcluedeId(setmealUpdateDTO.getName(), setmealUpdateDTO.getId());
        if (setmeal != null)
            throw new SetmealNameDuplicateException(ExceptionTipConstant.SETMEAL_NAME_DUPLICATE);

        // 向setmeal表中更新
        setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealUpdateDTO, setmeal);

        setmealMapper.update(setmeal);

        // 先删除setmeal_dish中的菜品
        setmealDishMapper.batchDelete(setmealUpdateDTO.getId());
        // 再插入菜品
        Object[] setmealDishList = setmealUpdateDTO.getSetmealDishes();
        setmealDishMapper.batchAdd(setmealDishList, setmeal.getId());

        // 从redis中删除副本
        stringRedisTemplate.delete("category-" + setmeal.getCategoryId());
    }

    /**
     * 获取套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO get(Long id) {
        // 查询得到套餐信息和分类名
        SetmealVO setmealVO = setmealMapper.get(id);
        // 获取套餐关联的菜品
        SetmealDish[] setmealDishes = setmealDishMapper.getListBySetmealId(id);

        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐状态
     *
     * @param id
     * @param status
     */
    @Override
    public void editStatus(Long id, Integer status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.editStatus(setmeal);

        // 从redis中删除副本
        stringRedisTemplate.delete("category-" + setmealMapper.getById(id).getCategoryId());
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Override
    @Transactional
    public void batchDelete(String ids) {
        // 获取id列表
        String[] idList = ids.split(",");

        // 从redis中删除副本
        List<Long> categoryIdList = setmealMapper.getCategotyId(idList);
        Set<String> categoryIdSet = categoryIdList.stream()
                .distinct()
                .map(id -> "category-" + id)
                .collect(Collectors.toSet());
        stringRedisTemplate.delete(categoryIdSet);

        // 删除套餐
        setmealMapper.batchDelete(idList);

        // 删除套餐相关的菜品
        setmealDishMapper.batchDeleteBySetmealList(idList);
    }

    /**
     * 用户根据分类查询套餐
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Setmeal> list(Long categoryId) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

        // 判断在redis中是否有副本
        String json = opsForValue.get("category-" + categoryId);
        if (json == null) {
            List<Setmeal> setmealList = setmealMapper.list(categoryId);
            // 加入redis中
            opsForValue.set("category-" + categoryId, JSON.toJSONString(setmealList));
            return setmealList;
        } else {
            System.out.println("套餐redis触发");
            List list = JSON.parseObject(json, List.class);
            return list;
        }
    }

    /**
     * 用户查看该套餐下的菜品
     *
     * @param id
     * @return
     */
    @Override
    public SetmealDishVO[] getDish(Long id) {
        SetmealDishVO[] setmealDishVOList = setmealDishMapper.getSetmealVOListBySetmealId(id);
        return setmealDishVOList;
    }
}
