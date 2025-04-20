package com.example.sky.task;

import com.example.sky.constant.OrderStatusConstant;
import com.example.sky.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 0 0 * * ?") // 每天12点自动执行
    public void orderTask() {
        // 自动将未完成的订单设置成已完成
        orderMapper.autoCompleted(OrderStatusConstant.Completed, OrderStatusConstant.Delivery_in_Progress);
    }
}
