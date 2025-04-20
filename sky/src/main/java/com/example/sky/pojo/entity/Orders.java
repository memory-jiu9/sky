package com.example.sky.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private Long id;
    private String number;
    private Integer status;
    private Long userId;
    private Long addressBookId;
    private LocalDateTime orderTime;
    private LocalDateTime checkoutTime;
    private Integer payMethod;
    private Integer payStatus;
    private BigDecimal amount;
    private String remark;
    private String phone;
    private String address;
    private String userName;
    private String consignee;
    private String cancelReason;
    private String rejectionReason;
    private LocalDateTime cancelTime;
    private LocalDateTime estimatedDeliveryTime;
    private Integer deliveryStatus;
    private LocalDateTime deliveryTime;
    private Integer packAmount;
    private Integer tablewareNumber;
    private Integer tablewareStatus;
}
