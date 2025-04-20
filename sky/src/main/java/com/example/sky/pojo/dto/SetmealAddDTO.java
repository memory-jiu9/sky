package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealAddDTO {
    private Long categoryId;
    private String description;
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Object[] setmealDishes;
    private Integer status;
}
