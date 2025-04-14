package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateDTO {
    private Long categoryId;
    private String description;
    private Object[] flavors;
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Integer status;
}
