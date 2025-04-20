package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsVO {
    private Integer confirmed;
    private Integer deliveryInProgress;
    private Integer toBeConfirmed;
}
