package com.example.sky.controller.admin;

import com.example.sky.pojo.dto.OrdersStatisticsDTO;
import com.example.sky.pojo.dto.Top10DTO;
import com.example.sky.pojo.dto.TurnoverStatisticsDTO;
import com.example.sky.pojo.dto.UserStatisticsDTO;
import com.example.sky.pojo.vo.OrdersStatisticsVO;
import com.example.sky.pojo.vo.Top10VO;
import com.example.sky.pojo.vo.TurnoverStatisticsVO;
import com.example.sky.pojo.vo.UserStatisticsVO;
import com.example.sky.service.ReportService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 查询菜品，套餐销量排名top10
     *
     * @param top10DTO
     * @return
     */
    @GetMapping("/top10")
    public Result<Top10VO> top10(Top10DTO top10DTO) {
        log.info("查询菜品，套餐销量排名top10：{}", top10DTO);
        Top10VO top10VO = reportService.top10(top10DTO);
        return Result.success(top10VO);
    }

    /**
     * 在时间区间内进行营业额统计
     *
     * @param turnoverStatisticsDTO
     * @return
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverStatisticsVO> turnoverStatistics(TurnoverStatisticsDTO turnoverStatisticsDTO) {
        log.info("在时间区间内进行营业额统计：{}", turnoverStatisticsDTO);
        TurnoverStatisticsVO turnoverStatisticsVO = reportService.turnoverStatistics(turnoverStatisticsDTO);
        return Result.success(turnoverStatisticsVO);
    }

    /**
     * 在时间区间内进行用户统计
     *
     * @param userStatisticsDTO
     * @return
     */
    @GetMapping("/userStatistics")
    public Result<UserStatisticsVO> userStatistics(UserStatisticsDTO userStatisticsDTO) {
        log.info("在时间区间内进行用户统计：{}", userStatisticsDTO);
        UserStatisticsVO userStatisticsVO = reportService.userStatistics(userStatisticsDTO);
        return Result.success(userStatisticsVO);
    }

    /**
     * 在时间区间内进行订单统计
     * @param ordersStatisticsDTO
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrdersStatisticsVO> ordersStatistics(OrdersStatisticsDTO ordersStatisticsDTO) {
        log.info("在时间区间内进行订单统计：{}", ordersStatisticsDTO);
        OrdersStatisticsVO ordersStatisticsVO = reportService.ordersStatistics(ordersStatisticsDTO);
        return Result.success(ordersStatisticsVO);
    }

}
