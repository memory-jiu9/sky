package com.example.sky.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.sky.constant.BaseContextConstant;
import com.example.sky.constant.WeChatAppConstant;
import com.example.sky.mapper.UserMapper;
import com.example.sky.pojo.dto.UserLoginDTO;
import com.example.sky.pojo.entity.User;
import com.example.sky.pojo.vo.UserLoginVO;
import com.example.sky.properties.JwtProperties;
import com.example.sky.properties.WeChatAppProperties;
import com.example.sky.service.UserService;
import com.example.sky.util.HttpClientUtil;
import com.example.sky.util.JwtUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private WeChatAppProperties weChatAppProperties;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 参数列表
        ArrayList<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair(WeChatAppConstant.APPID, weChatAppProperties.getAppid()));
        list.add(new BasicNameValuePair(WeChatAppConstant.SECRET, weChatAppProperties.getSecret()));
        list.add(new BasicNameValuePair(WeChatAppConstant.JS_CODE, userLoginDTO.getCode()));
        list.add(new BasicNameValuePair(WeChatAppConstant.GRANT_TYPE, WeChatAppConstant.AUTHORIZATION_CODE));

        // 发送请求
        String json = HttpClientUtil.get(WeChatAppConstant.URL, list);

        // 解析请求
        String openid = JSON.parseObject(json).getString(WeChatAppConstant.OPENID);

        // 判断是不是第一次登录
        User user = userMapper.getByOpenId(openid);
        if (user == null) {
            // 创建User实例
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            // 执行插入操作
            userMapper.add(user);
        }

        // 创建返回对象
        UserLoginVO userLoginVO = new UserLoginVO();

        // 获取token
        HashMap<String, Object> map = new HashMap<>();
        map.put(BaseContextConstant.ID, user.getId());
        String token = JwtUtil.getToken(jwtProperties.getUserSecretKey(), jwtProperties.getUserTimeout(), map);

        // 拷贝属性
        userLoginVO.setId(user.getId());
        userLoginVO.setOpenid(openid);
        userLoginVO.setToken(token);

        return userLoginVO;
    }
}
