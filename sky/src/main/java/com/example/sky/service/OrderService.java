package com.example.sky.service;

import com.example.sky.pojo.dto.*;
import com.example.sky.pojo.vo.*;

public interface OrderService {
    /**
     * 用户下单
     * @param orderAddDTO
     * @return
     */
    OrderSubmitVO order(OrderAddDTO orderAddDTO);

    /**
     * 用户支付
     * @param payDTO
     * @return
     */
    String pay(PayDTO payDTO);

    /**
     * 用户查询订单详情
     * @param id
     * @return
     */
    OrderVOForUser getForUser(Long id);

    /**
     * 用户取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 用户历史订单查询
     * @param userOrderPageQueryDTO
     * @return
     */
    PageQueryVO page(UserOrderPageQueryDTO userOrderPageQueryDTO);

    /**
     * 员工查询订单记录
     * @param adminOrderPageQueryDTO
     * @return
     */
    PageQueryVO page(AdminOrderPageQueryDTO adminOrderPageQueryDTO);

    /**
     * 用户催单
     * @param id
     */
    void remind(Long id);

    /**
     * 员工对各个状态的订单数量统计
     * @return
     */
    StatisticsVO statistics();

    /**
     * 员工查看订单的详细信息
     * @param id
     * @return
     */
    OrderInfoVO getForAdmin(Long id);

    /**
     * 接单
     * @param id
     */
    void confirm(Long id);

    /**
     * 拒单
     * @param rejectDTO
     */
    void reject(RejectDTO rejectDTO);

    /**
     * 派送订单
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     */
    void complete(Long id);
}
