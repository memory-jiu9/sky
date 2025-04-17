package com.example.sky.service;

import com.example.sky.pojo.dto.OrderAddDTO;
import com.example.sky.pojo.dto.OrderPageQueryDTO;
import com.example.sky.pojo.dto.PayDTO;
import com.example.sky.pojo.vo.OrderSubmitVO;
import com.example.sky.pojo.vo.OrderVO;
import com.example.sky.pojo.vo.PageQueryVO;

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
    OrderVO get(Long id);

    /**
     * 用户取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 用户历史订单查询
     * @param orderPageQueryDTO
     * @return
     */
    PageQueryVO page(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 用户催单
     * @param id
     */
    void remind(Long id);
}
