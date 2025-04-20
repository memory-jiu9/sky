package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployLoginVO {
    private Long id;
    private String name;
    private String token;
    private String username;
}
