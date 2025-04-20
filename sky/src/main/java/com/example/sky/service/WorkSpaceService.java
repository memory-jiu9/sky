package com.example.sky.service;

import com.example.sky.pojo.vo.BusinessDataVO;
import com.example.sky.pojo.vo.OverviewDishesVO;
import com.example.sky.pojo.vo.OverviewOrdersVO;
import com.example.sky.pojo.vo.OverviewSetmealsVO;

public interface WorkSpaceService {
    /**
     * 管理端获取今日运营数据
     * @return
     */
    BusinessDataVO getBusinessData();

    /**
     * 查询套餐总览
     * @return
     */
    OverviewSetmealsVO overviewSetmeals();

    /**
     * 查询菜品总览
     * @return
     */
    OverviewDishesVO overviewDishes();

    /**
     * 查询订单数据
     * @return
     */
    OverviewOrdersVO overviewOrders();
}
