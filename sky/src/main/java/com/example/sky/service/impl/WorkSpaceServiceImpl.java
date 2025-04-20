package com.example.sky.service.impl;

import com.example.sky.constant.DishStatusConstant;
import com.example.sky.constant.OrderStatusConstant;
import com.example.sky.constant.SetmealStatusConstant;
import com.example.sky.mapper.DishMapper;
import com.example.sky.mapper.OrderMapper;
import com.example.sky.mapper.SetmealMapper;
import com.example.sky.mapper.UserMapper;
import com.example.sky.pojo.entity.Orders;
import com.example.sky.pojo.vo.BusinessDataVO;
import com.example.sky.pojo.vo.OverviewDishesVO;
import com.example.sky.pojo.vo.OverviewOrdersVO;
import com.example.sky.pojo.vo.OverviewSetmealsVO;
import com.example.sky.service.WorkSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 管理端获取今日运营数据
     *
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {
        // 先获取时间
        LocalDateTime now = LocalDateTime.now(); // 当前时间
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT); // 今日开始时间

        // 获取所有今日创建且状态为已完成的订单
        List<Orders> finishOrderList = orderMapper.finishOrders(start, OrderStatusConstant.Completed);

        // 营业额，遍历finishOrderList获取收入总和
        BigDecimal turnover = BigDecimal.valueOf(0);
        for (Orders orders : finishOrderList) {
            turnover = turnover.add(orders.getAmount());
        }

        Integer newUsers = userMapper.newUsers(start); // 新增用户数


        BigDecimal finishOrders = null; // 已完成的订单
        BigDecimal orderCompletionRate = null; // 订单完成率
        BigDecimal unitPrice = null; // 平均单价
        BigDecimal newOrders = BigDecimal.valueOf(orderMapper.newOrders(start)); // 新增订单
        // 新增订单可能为0，避免出现0作为被除数
        if (newOrders.equals(BigDecimal.valueOf(0))) {
            finishOrders = BigDecimal.valueOf(0);
            orderCompletionRate = BigDecimal.valueOf(0);
            unitPrice = BigDecimal.valueOf(0);
        } else {
            finishOrders = BigDecimal.valueOf(finishOrderList.size());
            orderCompletionRate = finishOrders.divide(newOrders, 2, RoundingMode.FLOOR);
            if (finishOrders.equals(BigDecimal.valueOf(0)))
                unitPrice = BigDecimal.valueOf(0);
            else
                unitPrice = turnover.divide(finishOrders, 2, RoundingMode.FLOOR);
        }

        // 有效订单数：这个主要是看怎么定义什么是有效，什么是无效，我这里直接以订单完成作为有效条件
        Integer validOrderCount = finishOrderList.size();

        BusinessDataVO businessDataVO = new BusinessDataVO(newUsers, orderCompletionRate, turnover, unitPrice, validOrderCount);
        return businessDataVO;
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    @Override
    public OverviewSetmealsVO overviewSetmeals() {
        Integer discontinued = setmealMapper.getCountByStatus(SetmealStatusConstant.DISABLE);
        Integer sold = setmealMapper.getCountByStatus(SetmealStatusConstant.ENABLE);
        return new OverviewSetmealsVO(discontinued, sold);
    }

    /**
     * 查询菜品总览
     *
     * @return
     */
    @Override
    public OverviewDishesVO overviewDishes() {
        Integer discontinued = dishMapper.getCountByStatus(DishStatusConstant.DISABLE);
        Integer sold = dishMapper.getCountByStatus(DishStatusConstant.ENABLE);
        return new OverviewDishesVO(discontinued, sold);
    }

    /**
     * 查询订单数据
     *
     * @return
     */
    @Override
    public OverviewOrdersVO overviewOrders() {
        // 获取今天的数据
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT); // 今日开始时间

        Integer allOrders = orderMapper.newOrders(start);
        Integer cancelledOrders = orderMapper.getTodayCountByStatus(start, OrderStatusConstant.Cancelled);
        Integer completedOrders = orderMapper.getTodayCountByStatus(start, OrderStatusConstant.Completed);
        Integer deliveredOrders = orderMapper.getTodayCountByStatus(start, OrderStatusConstant.Received);
        Integer waitingOrders = orderMapper.getTodayCountByStatus(start, OrderStatusConstant.Pending_Receipt);

        return new OverviewOrdersVO(allOrders, cancelledOrders, completedOrders, deliveredOrders, waitingOrders);
    }
}
