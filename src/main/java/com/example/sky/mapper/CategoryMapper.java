package com.example.sky.mapper;

import com.example.sky.annotation.CreateAndUpdateAutoFill;
import com.example.sky.constant.OperationTypeConstant;
import com.example.sky.pojo.dto.CategoryPageQueryDTO;
import com.example.sky.pojo.entity.Category;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据分类名获取记录
     *
     * @param name
     * @return
     */
    @Select("select * from category where name = #{name}")
    Category getByName(String name);

    /**
     * 新增分类
     *
     * @param category
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.INSERT)
    void add(Category category);

    /**
     * 删除分类
     *
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    /**
     * 编辑分类状态
     *
     * @param category
     */
    @Update("update category set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void editStatus(Category category);

    /**
     * 根据分类名获取记录，排除指定的id
     *
     * @param name
     * @param id
     * @return
     */
    @Select("select * from category where name = #{name} and id != #{id}")
    Category getByNameExcludeId(String name, Long id);

    /**
     * 修改分类
     * @param category
     */
    @Update("update category set name = #{name}, sort = #{sort}, type = #{type}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void update(Category category);

    /**
     * 获取分类列表
     * @param type
     * @return
     */
    @Select("select * from category where type = #{type}")
    List<Category> listForAdmin(Integer type);

    /**
     * 根据id获取分类名
     * @param categoryId
     * @return
     */
    @Select("select name from category where id = #{categoryId}")
    String getNameById(Long categoryId);

    /**
     * 获取分类列表
     * @param type
     * @return
     */
    @Select("select * from category where type = #{type} and status = 1")
    List<Category> listForUser(Integer type);

    /**
     * 获取分类列表：无参数
     * @return
     */
    @Select("select * from category where status = 1")
    List<Category> listForUserWithoutType();
}
