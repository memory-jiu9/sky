package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderPageQueryDTO {
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String number;
    private Integer page;
    private Integer pageSize;
    private String phone;
    private Integer status;
}
