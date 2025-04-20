package com.example.sky.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishFlavor {
    private Long id;
    private Long dishId;
    private String name;
    private String value;
}
