package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddDTO {
    private Long addressBookId;
    private BigDecimal amount;
    private Integer deliveryStatus;
    private String estimatedDeliveryTime;
    private Integer packAmount;
    private Integer payMethod;
    private String remark;
    private Integer tablewareNumber;
    private Integer tablewareStatus;
}
