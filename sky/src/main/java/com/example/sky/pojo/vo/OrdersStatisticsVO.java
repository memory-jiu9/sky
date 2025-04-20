package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersStatisticsVO {
    private String dateList;
    private String orderCountList;
    private BigDecimal orderCompletionRate;
    private Integer totalOrderCount;
    private Integer validOrderCount;
    private String validOrderCountList;
}
