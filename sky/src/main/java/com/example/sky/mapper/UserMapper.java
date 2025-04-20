package com.example.sky.mapper;

import com.example.sky.pojo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    /**
     * 根据openid获取记录
     *
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openid);

    /**
     * 将用户插入user表
     *
     * @param user
     */
    void add(User user);

    /**
     * 获取新增用户数
     *
     * @return
     */
    @Select("select count(*) from user where create_time > #{start}")
    Integer newUsers(LocalDateTime start);

    /**
     * 获取指定时间区间内的新增用户数
     * @param begin
     * @param end
     * @return
     */
    @Select("select count(*) from user where create_time > #{begin} and create_time < #{end}")
    Integer getTimePeriodNewUser(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取指定时间区间内的总用户数
     * @param begin
     * @param end
     * @return
     */
    @Select("select count(*) from user where create_time < #{end}")
    Integer getTimePeriodTotalUser(LocalDateTime end);
}
