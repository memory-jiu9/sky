package com.example.sky.service;

import com.example.sky.pojo.dto.OrdersStatisticsDTO;
import com.example.sky.pojo.dto.Top10DTO;
import com.example.sky.pojo.dto.TurnoverStatisticsDTO;
import com.example.sky.pojo.dto.UserStatisticsDTO;
import com.example.sky.pojo.vo.OrdersStatisticsVO;
import com.example.sky.pojo.vo.Top10VO;
import com.example.sky.pojo.vo.TurnoverStatisticsVO;
import com.example.sky.pojo.vo.UserStatisticsVO;

public interface ReportService {
    /**
     * 查询菜品，套餐销量排名top10
     * @param top10DTO
     * @return
     */
    Top10VO top10(Top10DTO top10DTO);

    /**
     * 在时间区间内进行营业额统计
     * @param turnoverStatisticsDTO
     * @return
     */
    TurnoverStatisticsVO turnoverStatistics(TurnoverStatisticsDTO turnoverStatisticsDTO);

    /**
     * 在时间区间内进行用户统计
     * @param userStatisticsDTO
     * @return
     */
    UserStatisticsVO userStatistics(UserStatisticsDTO userStatisticsDTO);

    /**
     * 在时间区间内进行订单统计
     * @param ordersStatisticsDTO
     * @return
     */
    OrdersStatisticsVO ordersStatistics(OrdersStatisticsDTO ordersStatisticsDTO);
}
