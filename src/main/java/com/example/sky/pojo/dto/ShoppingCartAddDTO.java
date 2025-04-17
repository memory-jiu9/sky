package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartAddDTO {
    private String dishFlavor;
    private Long dishId;
    private Long setmealId;
}
