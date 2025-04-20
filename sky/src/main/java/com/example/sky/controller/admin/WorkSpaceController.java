package com.example.sky.controller.admin;

import com.example.sky.pojo.vo.BusinessDataVO;
import com.example.sky.pojo.vo.OverviewDishesVO;
import com.example.sky.pojo.vo.OverviewOrdersVO;
import com.example.sky.pojo.vo.OverviewSetmealsVO;
import com.example.sky.service.WorkSpaceService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 获取今日运营数据
     *
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData() {
        log.info("管理端获取今日运营数据");
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData();
        return Result.success(businessDataVO);
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    @GetMapping("/overviewSetmeals")
    public Result<OverviewSetmealsVO> overviewSetmeals() {
        log.info("查询套餐总览");
        OverviewSetmealsVO overviewSetmealsVO = workSpaceService.overviewSetmeals();
        return Result.success(overviewSetmealsVO);
    }

    /**
     * 查询菜品总览
     *
     * @return
     */
    @GetMapping("/overviewDishes")
    public Result<OverviewDishesVO> overviewDishes() {
        log.info("查询菜品总览");
        OverviewDishesVO overviewDishesVO = workSpaceService.overviewDishes();
        return Result.success(overviewDishesVO);
    }

    /**
     * 查询订单数据
     *
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OverviewOrdersVO> overviewOrders() {
        log.info("查询菜品总览");
        OverviewOrdersVO overviewOrdersVO = workSpaceService.overviewOrders();
        return Result.success(overviewOrdersVO);
    }
}
