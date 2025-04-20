package com.example.sky.service;

import com.example.sky.pojo.dto.UserLoginDTO;
import com.example.sky.pojo.vo.UserLoginVO;

public interface UserService {
    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
