package com.example.sky.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsVO {
    private String dateList;
    private String newUserList;
    private String totalUserList;
}
