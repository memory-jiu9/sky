package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverviewOrdersVO {
    private Integer allOrders;
    private Integer cancelledOrders;
    private Integer completedOrders;
    private Integer deliveredOrders;
    private Integer waitingOrders;
}
