package com.example.sky.mapper;

import com.example.sky.pojo.dto.AdminOrderPageQueryDTO;
import com.example.sky.pojo.dto.DishSaleStatisticsDTO;
import com.example.sky.pojo.dto.RejectDTO;
import com.example.sky.pojo.entity.Orders;
import com.example.sky.pojo.vo.OrderVOForAdmin;
import com.example.sky.pojo.vo.OrderVOForUser;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 用户下单
     *
     * @param orders
     */
    void add(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     * @return
     */
    @Select("select id,estimated_delivery_time from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 支付成功：修改订单
     *
     * @param orders
     */
    void pay(Orders orders);

    /**
     * 根据订单id获取订单记录
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 用户取消订单
     *
     * @param id
     * @param status
     * @param cancelTime
     * @param payStatus
     */
    @Update("update orders set status = #{status}, cancel_time = #{cancelTime}, pay_status = #{payStatus} where id = #{id}")
    void cancel(Long id, Integer status, LocalDateTime cancelTime, Integer payStatus);

    /**
     * 用户历史订单查询
     *
     * @param userId
     * @param status
     * @return
     */
    Page<OrderVOForUser> pageForUser(Long userId, Integer status);

    /**
     * 员工查询订单
     *
     * @return
     */
    Page<OrderVOForAdmin> pageForAdmin(AdminOrderPageQueryDTO adminOrderPageQueryDTO);

    /**
     * 根据订单状态返回记录数
     *
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer getCountByStatus(Integer status);

    /**
     * 接单
     *
     * @param id
     * @param status
     */
    @Update("update orders set status = #{status} where id = #{id}")
    void confirm(Long id, Integer status);

    /**
     * 拒单
     *
     * @param id
     * @param rejectionReason
     * @param status
     */
    @Update("update orders set status = #{status}, rejection_reason = #{rejectionReason} where id = #{id}")
    void reject(Long id, String rejectionReason, Integer status);

    /**
     * 派送订单
     *
     * @param id
     * @param status
     */
    @Update("update orders set status = #{status} where id = #{id}")
    void delivery(Long id, Integer status);

    /**
     * 派送订单
     *
     * @param id
     * @param status
     */
    @Update("update orders set status = #{status} where id = #{id}")
    void complete(Long id, Integer status);

    /**
     * 获取新增订单数
     *
     * @param start
     * @return
     */
    @Select("select count(*) from orders where order_time > #{start}")
    Integer newOrders(LocalDateTime start);

    /**
     * 获取已完成订单记录
     *
     * @param start
     * @param status
     * @return
     */
    @Select("select * from orders where order_time > #{start} and status = #{status}")
    List<Orders> finishOrders(LocalDateTime start, Integer status);

    /**
     * 根据订单状态获取记录数
     *
     * @param start
     * @param status
     * @return
     */
    @Select("select count(*) from orders where order_time > #{start} and status = #{status}")
    Integer getTodayCountByStatus(LocalDateTime start, Integer status);

    /**
     * 根据时间间隔获取所有已完成的订单记录
     *
     * @param begin
     * @param end
     * @param completed
     * @return
     */
    @Select("select * from orders where order_time > #{begin} and #{end} > order_time and status = #{status}")
    List<Orders> getIntervalCompletedList(LocalDate begin, LocalDate end, Integer completed);

    /**
     * 将未完成的订单修改成已完成
     *
     * @param completed
     * @param deliveryInProgress
     */
    @Update("update orders set status = #{completed} where status = #{deliveryInProgress}")
    void autoCompleted(Integer completed, Integer deliveryInProgress);

    /**
     * 获取指定时间区间内的营业额总数
     *
     * @param begin
     * @param end
     * @param status
     * @return
     */
    @Select("select sum(amount) from orders where order_time > #{begin} and order_time < #{end} and status = #{status}")
    BigDecimal getTimePeriodAmount(LocalDateTime begin, LocalDateTime end, Integer status);

    /**
     * 获取指定时间区间内的订单总数
     * @param begin
     * @param end
     * @return
     */
    @Select("select count(*) from orders where order_time > #{begin} and order_time < #{end}")
    Integer getTimePeriodOrderCount(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取指定时间区间内的有效订单总数
     * @param begin
     * @param end
     * @param status
     * @return
     */
    @Select("select count(*) from orders where order_time > #{begin} and order_time < #{end} and status = #{status}")
    Integer getTimePeriodVaildOrderCount(LocalDateTime begin, LocalDateTime end, Integer status);

    /**
     * 查询菜品，套餐销量排名top10
     * @param begin
     * @param end
     * @return
     */
    List<DishSaleStatisticsDTO> top10(LocalDateTime begin, LocalDateTime end);
}
