package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDataVO {
    private Integer newUsers;
    private BigDecimal orderCompletionRate;
    private BigDecimal turnover;
    private BigDecimal unitPrice;
    private Integer validOrderCount;
}
