package com.example.sky.mapper;

import com.example.sky.annotation.CreateAndUpdateAutoFill;
import com.example.sky.constant.OperationTypeConstant;
import com.example.sky.pojo.dto.SetmealPageQueryDTO;
import com.example.sky.pojo.entity.Setmeal;
import com.example.sky.pojo.entity.SetmealDish;
import com.example.sky.pojo.vo.SetmealPageQueryVO;
import com.example.sky.pojo.vo.SetmealVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealPageQueryVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据套餐名获取记录
     *
     * @param name
     * @return
     */
    @Select("select * from setmeal where name = #{name}")
    Setmeal getByName(String name);

    /**
     * 新增套餐
     *
     * @param setmeal
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.INSERT)
    void add(Setmeal setmeal);

    /**
     * 根据套餐名获取记录，排除给入的id
     *
     * @param name
     * @param id
     * @return
     */
    @Select("select * from setmeal where name = #{name} and id != #{id}")
    Setmeal getByNameExcluedeId(String name, Long id);

    /**
     * 修改套餐
     *
     * @param setmeal
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 获取套餐信息
     *
     * @param id
     * @return
     */
    SetmealVO get(Long id);

    /**
     * 修改套餐状态
     *
     * @param setmeal
     */
    @Update("update setmeal set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void editStatus(Setmeal setmeal);

    /**
     * 批量删除套餐
     *
     * @param idList
     */
    void batchDelete(String[] idList);

    /**
     * 用户根据分类查询套餐
     *
     * @param categoryId
     * @return
     */
    @Select("select * from setmeal where category_id = #{categoryId} and status = 1")
    List<Setmeal> list(Long categoryId);

    /**
     * 根据id获取套餐名
     *
     * @param setmealId
     * @return
     */
    @Select("select name from setmeal where id = #{setmealId}")
    String getNameById(Long setmealId);

    /**
     * 根据id获取记录
     *
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal where id = #{setmealId}")
    Setmeal getById(Long setmealId);

    /**
     * 根据套餐状态获取记录总数
     *
     * @param status
     * @return
     */
    @Select("select count(*) from setmeal where status = #{status}")
    Integer getCountByStatus(Integer status);

    /**
     * 获取套餐的分类id
     * @param idList
     * @return
     */
    List<Long> getCategotyId(String[] idList);
}
