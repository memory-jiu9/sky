package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAddDTO {
    private Long id;
    private String idNumber;
    private String name;
    private String phone;
    private String sex;
    private String username;
}
