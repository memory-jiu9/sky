package com.example.sky.controller.user;

import com.example.sky.pojo.dto.OrderAddDTO;
import com.example.sky.pojo.dto.UserOrderPageQueryDTO;
import com.example.sky.pojo.dto.PayDTO;
import com.example.sky.pojo.vo.OrderSubmitVO;
import com.example.sky.pojo.vo.OrderVOForUser;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.service.OrderService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param orderAddDTO
     * @return
     */
    @PostMapping("/submit")
    public Result order(@RequestBody OrderAddDTO orderAddDTO) {
        log.info("用户下单：{}", orderAddDTO);
        OrderSubmitVO orderSubmitVO = orderService.order(orderAddDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 用户支付
     *
     * @param payDTO
     * @return
     */
    @PutMapping("/payment")
    public Result<String> pay(@RequestBody PayDTO payDTO) {
        log.info("用户支付：{}", payDTO);
        String estimatedDeliveryTime = orderService.pay(payDTO);
        return Result.success(estimatedDeliveryTime);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVOForUser> get(@PathVariable Long id) {
        log.info("用户查询订单详情，订单id：{}", id);
        OrderVOForUser orderVOForUser = orderService.getForUser(id);
        return Result.success(orderVOForUser);
    }

    /**
     * 取消订单
     *
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) {
        log.info("用户取消订单，订单id：{}", id);
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * 再来一单
     *
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    public Result repeat(@PathVariable Long id) {
        log.info("再来一单：{}", id);
        // 这里应该不需要任何操作，用户点击“再来一单”后，会清空购物车，然后重新跳转到首页
        return Result.success();
    }

    /**
     * 用户历史订单查询
     *
     * @param userOrderPageQueryDTO
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageQueryVO> page(UserOrderPageQueryDTO userOrderPageQueryDTO) {
        log.info("用户历史订单查询：{}", userOrderPageQueryDTO);
        PageQueryVO pageQueryVO = orderService.page(userOrderPageQueryDTO);
        return Result.success(pageQueryVO);
    }

    /**
     * 用户催单
     *
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    public Result remind(@PathVariable Long id) {
        log.info("用户催单，订单id：{}", id);
        orderService.remind(id);
        return Result.success();
    }
}
