package com.example.sky.mapper;

import com.example.sky.annotation.CreateAndUpdateAutoFill;
import com.example.sky.constant.OperationTypeConstant;
import com.example.sky.pojo.dto.DishPageQueryDTO;
import com.example.sky.pojo.entity.Dish;
import com.example.sky.pojo.vo.DishPageQueryVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishPageQueryVO> page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 编辑菜品状态
     *
     * @param dish
     */
    @Update("update dish set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void editStatus(Dish dish);

    /**
     * 根据id获取记录
     *
     * @param id
     * @return
     */
    @Select("select category_id,description,id,image,name,price,status,update_time from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 获取一个分类下的菜品总数
     *
     * @param id
     * @return
     */
    @Select("select count(*) from dish where category_id = #{id}")
    Integer getCountByCategoryId(Long id);

    /**
     * 根据菜品名称获取记录
     *
     * @param name
     * @return
     */
    @Select("select * from dish where name = #{name}")
    Dish getByName(String name);

    /**
     * 新增菜品
     *
     * @param dish
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.INSERT)
    void add(Dish dish);

    /**
     * 排除菜品id，并根据菜品名称获取记录
     *
     * @param name
     * @param id
     * @return
     */
    @Select("select * from dish where name = #{name} and id != #{id}")
    Dish getByNameExcludeId(String name, Long id);

    /**
     * 修改菜品信息
     *
     * @param dish
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void update(Dish dish);

    /**
     * 根据id列表批量删菜品
     *
     * @param idList
     */
    void batchDelete(String[] idList);

    /**
     * 根据分类id获取菜品列表，菜品的状态要是起售中
     *
     * @param categoryId
     * @return
     */
    @Select("select * from dish where category_id = #{categoryId} and status = 1")
    List<Dish> list(Long categoryId);

    /**
     * 根据菜品状态获取记录总数
     *
     * @param status
     * @return
     */
    @Select("select count(*) from dish where status = #{status}")
    Integer getCountByStatus(Integer status);

    /**
     * 获取菜品对应的分类id
     * @param idList
     * @return
     */
    List<Long> getCategoryId(String[] idList);
}
