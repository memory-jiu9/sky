package com.example.sky.mapper;

import com.example.sky.pojo.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 根据菜品id获取口味列表
     *
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);

    /**
     * 对指定菜品添加口味列表
     *
     * @param flavors
     */
    void batchAdd(Object[] flavors, Long id);

    /**
     * 根据菜品id删除口味数据
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id = #{id}")
    void batchDeleteById(Long id);

    /**
     * 根据菜品id列表删除口味数据
     * @param idList
     */
    void batchDeleteByIdList(String[] idList);
}
