package com.example.sky.mapper;

import com.example.sky.pojo.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {
    /**
     * 批量添加套餐相关菜品
     *
     * @param setmealDishList
     * @param id
     */
    void batchAdd(Object[] setmealDishList, Long id);

    /**
     * 批量删除一个套餐的相关菜品
     *
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void batchDelete(Long id);

    /**
     * 获取一个套餐的相关菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    SetmealDish[] getListBySetmealId(Long id);

    /**
     * 批量删除套餐时，删除套餐相关的菜品
     * @param idList
     */
    void batchDeleteBySetmealList(String[] idList);
}
