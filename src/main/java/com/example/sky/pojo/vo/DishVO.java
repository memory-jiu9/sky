package com.example.sky.pojo.vo;

import com.example.sky.pojo.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishVO {
    private Long categoryId;
    private String categoryName;
    private String description;
    private List<DishFlavor> flavors;
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Integer status;
    private LocalDateTime updateTime;
}
