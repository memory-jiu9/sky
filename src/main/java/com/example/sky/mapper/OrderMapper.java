package com.example.sky.mapper;

import com.example.sky.pojo.entity.Orders;
import com.example.sky.pojo.vo.OrderVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface OrderMapper {
    /**
     * 用户下单
     * @param orders
     */
    void add(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return
     */
    @Select("select id,estimated_delivery_time from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 支付成功：修改订单
     * @param orders
     */
    void pay(Orders orders);

    /**
     * 根据订单id获取订单记录
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 用户取消订单
     * @param id
     * @param status
     * @param cancelTime
     * @param payStatus
     */
    @Update("update orders set status = #{status}, cancel_time = #{cancelTime}, pay_status = #{payStatus} where id = #{id}")
    void cancel(Long id, Integer status, LocalDateTime cancelTime, Integer payStatus);

    /**
     * 用户历史订单查询
     * @param userId
     * @param status
     * @return
     */
    Page<OrderVO> page(Long userId, Integer status);
}
