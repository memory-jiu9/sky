package com.example.sky.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookUpdateDTO {
    private String cityCode;
    private String cityName;
    private String consignee;
    private String detail;
    private String districtCode;
    private String districtName;
    private Long id;
    private Integer isDefault;
    private String label;
    private String phone;
    private String provinceCode;
    private String provinceName;
    private String sex;
    private Long userId;
}
