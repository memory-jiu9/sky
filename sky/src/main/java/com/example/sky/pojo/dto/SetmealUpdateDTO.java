package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealUpdateDTO {
    private Long categoryId;
    private String description;
    private String categoryName;
    private Integer idType;
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Object[] setmealDishes;
    private Integer status;
}
