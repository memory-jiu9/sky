package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealPageQueryDTO {
    private Long categoryId;
    private String name;
    private Integer page;
    private Integer pageSize;
    private Integer status;
}
