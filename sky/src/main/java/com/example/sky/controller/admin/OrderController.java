package com.example.sky.controller.admin;

import com.example.sky.pojo.dto.AdminOrderPageQueryDTO;
import com.example.sky.pojo.dto.ConfirmDTO;
import com.example.sky.pojo.dto.RejectDTO;
import com.example.sky.pojo.vo.OrderInfoVO;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.pojo.vo.StatisticsVO;
import com.example.sky.service.OrderService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 员工查询订单情况
     *
     * @param adminOrderPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageQueryVO> page(AdminOrderPageQueryDTO adminOrderPageQueryDTO) {
        log.info("员工查询订单情况：{}", adminOrderPageQueryDTO);
        PageQueryVO pageQueryVO = orderService.page(adminOrderPageQueryDTO);
        return Result.success(pageQueryVO);
    }

    /**
     * 员工对各个状态的订单数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    public Result<StatisticsVO> statistics() {
        log.info("员工对各个状态的订单数量统计");
        StatisticsVO statisticsVO = orderService.statistics();
        return Result.success();
    }

    /**
     * 员工查看订单的详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderInfoVO> get(@PathVariable Long id) {
        log.info("员工查看订单的详细信息，订单id：{}", id);
        OrderInfoVO orderInfoVO = orderService.getForAdmin(id);
        return Result.success(orderInfoVO);
    }

    /**
     * 接单
     *
     * @param confirmDTO
     * @return
     */
    @PutMapping("/confirm")
    public Result confirm(@RequestBody ConfirmDTO confirmDTO) {
        log.info("管理端接单，订单id：{}", confirmDTO.getId());
        orderService.confirm(confirmDTO.getId());
        return Result.success();
    }

    /**
     * 拒单
     *
     * @param rejectDTO
     * @return
     */
    @PutMapping("/rejection")
    public Result reject(@RequestBody RejectDTO rejectDTO) {
        log.info("管理端拒单，订单id：{}", rejectDTO.getId());
        orderService.reject(rejectDTO);
        return Result.success();
    }

    /**
     * 派送订单
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id) {
        log.info("管理端派送订单，订单id：{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        log.info("管理端完成订单，订单id：{}", id);
        orderService.complete(id);
        return Result.success();
    }

}
