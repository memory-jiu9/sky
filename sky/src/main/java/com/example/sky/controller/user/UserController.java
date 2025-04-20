package com.example.sky.controller.user;

import com.example.sky.pojo.dto.UserLoginDTO;
import com.example.sky.pojo.vo.UserLoginVO;
import com.example.sky.service.UserService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    /**
     * 用户退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        log.info("用户退出");
        return Result.success();
    }
}
