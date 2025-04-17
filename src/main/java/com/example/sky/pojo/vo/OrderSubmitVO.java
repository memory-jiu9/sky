package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSubmitVO {
    private Long id;
    private BigDecimal orderAmount;
    private String orderNumber;
    private String orderTime;
}
