package com.example.sky.service.impl;

import com.example.sky.constant.OrderStatusConstant;
import com.example.sky.mapper.OrderMapper;
import com.example.sky.mapper.UserMapper;
import com.example.sky.pojo.dto.*;
import com.example.sky.pojo.vo.OrdersStatisticsVO;
import com.example.sky.pojo.vo.Top10VO;
import com.example.sky.pojo.vo.TurnoverStatisticsVO;
import com.example.sky.pojo.vo.UserStatisticsVO;
import com.example.sky.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询菜品，套餐销量排名top10
     *
     * @param top10DTO
     * @return
     */
    @Override
    public Top10VO top10(Top10DTO top10DTO) {
        LocalDateTime begin = LocalDateTime.of(top10DTO.getBegin(), LocalTime.MIN); // 0:0:0
        LocalDateTime end = LocalDateTime.of(top10DTO.getEnd(), LocalTime.MAX); // 23:59:59
        List<DishSaleStatisticsDTO> top10List = orderMapper.top10(begin, end);

        StringJoiner nameListSJ = new StringJoiner(",");
        StringJoiner numberListSJ = new StringJoiner(",");
        // 拼接字符串
        for (DishSaleStatisticsDTO dishSaleStatisticsDTO : top10List) {
            nameListSJ.add(dishSaleStatisticsDTO.getName());
            numberListSJ.add(dishSaleStatisticsDTO.getNumber() + "");
        }

        return new Top10VO(nameListSJ.toString(), numberListSJ.toString());
    }

    /**
     * 在时间区间内进行营业额统计
     *
     * @param turnoverStatisticsDTO
     * @return
     */
    @Override
    public TurnoverStatisticsVO turnoverStatistics(TurnoverStatisticsDTO turnoverStatisticsDTO) {
        List<LocalDate> dateList = getDateList(turnoverStatisticsDTO.getBegin(), turnoverStatisticsDTO.getEnd());
        List<BigDecimal> amountList = new ArrayList<>();

        // 根据日期列表获取每日营业额
        for (LocalDate date : dateList) {
            // 计算得到开始时间和结束时间
            LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN); // 0:0:0
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59
            BigDecimal amount = orderMapper.getTimePeriodAmount(begin, end, OrderStatusConstant.Completed);
            amountList.add(amount);
        }

        return new TurnoverStatisticsVO(getDateListString(dateList), getBigDecimalListString(amountList));
    }

    /**
     * 在时间区间内进行用户统计
     *
     * @param userStatisticsDTO
     * @return
     */
    @Override
    public UserStatisticsVO userStatistics(UserStatisticsDTO userStatisticsDTO) {
        List<LocalDate> dateList = getDateList(userStatisticsDTO.getBegin(), userStatisticsDTO.getEnd());
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        // 根据日期列表获取每日营业额
        for (LocalDate date : dateList) {
            // 计算得到开始时间和结束时间
            LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN); // 0:0:0
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59

            Integer newUsers = userMapper.getTimePeriodNewUser(begin, end);
            Integer totalUsers = userMapper.getTimePeriodTotalUser(end);

            newUserList.add(newUsers);
            totalUserList.add(totalUsers);
        }

        return new UserStatisticsVO(getDateListString(dateList), getNumberListString(newUserList), getNumberListString(totalUserList));
    }

    /**
     * 在时间区间内进行订单统计
     *
     * @param ordersStatisticsDTO
     * @return
     */
    @Override
    public OrdersStatisticsVO ordersStatistics(OrdersStatisticsDTO ordersStatisticsDTO) {
        List<LocalDate> dateList = getDateList(ordersStatisticsDTO.getBegin(), ordersStatisticsDTO.getEnd());
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        Integer orderCountSum = 0; // 订单总数
        Integer validOrderCountSum = 0; // 有效订单数

        // 根据日期列表获取每日营业额
        for (LocalDate date : dateList) {
            // 计算得到开始时间和结束时间
            LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN); // 0:0:0
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59


            Integer orderCount = orderMapper.getTimePeriodOrderCount(begin, end);
            Integer validOrderCount = orderMapper.getTimePeriodVaildOrderCount(begin, end, OrderStatusConstant.Completed);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);

            orderCountSum += orderCount;
            validOrderCountSum += validOrderCount;
        }

        // 计算订单完成率
        BigDecimal orderCompletionRate = BigDecimal.valueOf(validOrderCountSum).divide(BigDecimal.valueOf(orderCountSum), 2, RoundingMode.FLOOR);

        return new OrdersStatisticsVO(getDateListString(dateList), getNumberListString(orderCountList), orderCompletionRate, orderCountSum, validOrderCountSum, getNumberListString(validOrderCountList));
    }

    /**
     * 获取时间区间列表
     *
     * @param begin
     * @param end
     * @return
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        // 获取日期列表
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        dateList.add(end);

        return dateList;
    }

    /**
     * 传递一个LocalDate列表，返回该列表的字符串形式，用逗号分隔
     * @param dateList
     * @return
     */
    private String getDateListString(List<LocalDate> dateList) {
        String[] dateArray = dateList.stream()
                .map(date -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return formatter.format(date);
                })
                .toArray(String[]::new);
        String dateListString = String.join(",", dateArray);
        return dateListString;
    }

    /**
     * 传递一个Integer列表，返回该列表的字符串形式，用逗号分隔
     * @param numberList
     * @return
     */
    private String getNumberListString(List<Integer> numberList){
        String[] numberArray = numberList.stream()
                .map(number -> number == null ? "0" : number.toString())
                .toArray(String[]::new);
        String numberListString = String.join(",", numberArray);
        return numberListString;
    }

    /**
     * 传递一个BigDecimal列表，返回该列表的字符串形式，用逗号分隔
     * @param numberList
     * @return
     */
    private String getBigDecimalListString(List<BigDecimal> numberList){
        String[] numberArray = numberList.stream()
                .map(number -> number == null ? "0" : number.toString())
                .toArray(String[]::new);
        String numberListString = String.join(",", numberArray);
        return numberListString;
    }

}
