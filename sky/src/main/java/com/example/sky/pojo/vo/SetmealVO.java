package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealVO {
    private Long categoryId;
    private String categoryName;
    private String description;
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Object[] setmealDishes;
    private Integer status;
    private LocalDateTime updateTime;
}
