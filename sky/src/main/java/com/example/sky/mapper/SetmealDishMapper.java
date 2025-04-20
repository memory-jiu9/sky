package com.example.sky.mapper;

import com.example.sky.pojo.entity.SetmealDish;
import com.example.sky.pojo.vo.SetmealDishVO;
import com.example.sky.pojo.vo.SetmealVO;
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

    /**
     * 根据菜品id查找套餐id
     * @param id
     * @return
     */
    @Select("select setmeal_id from setmeal_dish where dish_id = #{id}")
    Long getByDishId(Long id);

    /**
     * 获取套餐菜品视图列表
     * @param id
     * @return
     */
    @Select("select s.copies, d.description, d.image, d.name from setmeal_dish s inner join dish d on (s.dish_id = d.id) where s.setmeal_id = #{id}")
    SetmealDishVO[] getSetmealVOListBySetmealId(Long id);
}
