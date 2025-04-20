package com.example.sky.mapper;

import com.example.sky.pojo.entity.AddressBook;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AddressBookMapper {
    /**
     * 用户获取所有的地址列表
     *
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    AddressBook[] list(Long userId);

    /**
     * 获取默认地址
     *
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefault(Long userId);

    /**
     * 根据id获取地址
     *
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 设置默认地址
     *
     * @param id
     */
    @Update("update address_book set is_default = 1 where id = #{id}")
    void setDefault(Long id);

    /**
     * 将该用户的所有地址全部设为非默认
     *
     * @param userId
     */
    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void cancelDefault(Long userId);

    /**
     * 根据id删除地址
     *
     * @param id
     */
    @Delete("delete from address_book where id = #{id}")
    void delete(Long id);

    /**
     * 新增地址
     *
     * @param addressBook
     */
    @Insert("insert into address_book values (#{id},#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void add(AddressBook addressBook);

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);
}
