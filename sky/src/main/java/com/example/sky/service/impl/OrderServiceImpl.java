package com.example.sky.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.sky.constant.WebSocketMessageConstant;
import com.example.sky.constant.OrderStatusConstant;
import com.example.sky.constant.PayStatusConstant;
import com.example.sky.context.BaseContext;
import com.example.sky.mapper.AddressBookMapper;
import com.example.sky.mapper.OrderDetailMapper;
import com.example.sky.mapper.OrderMapper;
import com.example.sky.mapper.ShoppingCartMapper;
import com.example.sky.pojo.dto.*;
import com.example.sky.pojo.entity.AddressBook;
import com.example.sky.pojo.entity.OrderDetail;
import com.example.sky.pojo.entity.Orders;
import com.example.sky.pojo.entity.ShoppingCart;
import com.example.sky.pojo.vo.*;
import com.example.sky.service.OrderService;
import com.example.sky.websocket.WebSocketServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     *
     * @param orderAddDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO order(OrderAddDTO orderAddDTO) {
        // 创建Orders实例
        Orders orders = new Orders();
        // 获取用户id
        Long userId = BaseContext.getUserId();

        // 获取地址记录
        AddressBook addressBook = addressBookMapper.getById(orderAddDTO.getAddressBookId());

        // 获取订单信息
        OrderSubmitVO orderSubmitVO = pay();

        // 拷贝属性
        orders.setStatus(OrderStatusConstant.Pending_Payment); // 待支付状态
        orders.setUserId(userId);
        orders.setNumber(orderSubmitVO.getOrderNumber());
        orders.setAddressBookId(orderAddDTO.getAddressBookId());
        // 解析时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        orders.setOrderTime(LocalDateTime.parse(orderSubmitVO.getOrderTime(), formatter));
        orders.setPayMethod(orderAddDTO.getPayMethod());
        orders.setPayStatus(PayStatusConstant.Pending_Payment); // 未付款
        orders.setAmount(orderAddDTO.getAmount());
        orders.setRemark(orderAddDTO.getRemark());

        // 拷贝地址属性
        orders.setPhone(addressBook.getPhone());
        // 拼接地址
        StringBuilder sb = new StringBuilder();
        String address = sb.append(addressBook.getProvinceName())
                .append(addressBook.getCityName())
                .append(addressBook.getDistrictName())
                .append(addressBook.getDetail())
                .toString();
        orders.setAddress(address);
        // TODO 获取用户名 username
        orders.setConsignee(addressBook.getConsignee());
        orders.setDeliveryStatus(orderAddDTO.getDeliveryStatus());
        orders.setEstimatedDeliveryTime(LocalDateTime.parse(orderAddDTO.getEstimatedDeliveryTime(), formatter));
        orders.setPackAmount(orderAddDTO.getPackAmount());
        orders.setTablewareNumber(orderAddDTO.getTablewareNumber());
        orders.setTablewareStatus(orderAddDTO.getTablewareStatus());

        // 插入到orders表中
        orderMapper.add(orders);

        // 获取该用户的所有购物车，有多少个购物车就有多少条orders_detail记录
        ShoppingCart[] shoppingCarts = shoppingCartMapper.list(userId);
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        // 遍历ShoppingCart[]
        for (ShoppingCart shoppingCart : shoppingCarts) {
            // 创建OrderDetail实例
            OrderDetail orderDetail = new OrderDetail();

            // 拷贝属性
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());

            // 加入到集合中
            orderDetails.add(orderDetail);
        }

        // 插入到orders_detail表中
        orderDetailMapper.batchAdd(orderDetails);

        orderSubmitVO.setId(orders.getId());

        return orderSubmitVO;
    }

    /**
     * 用户支付
     *
     * @param payDTO
     * @return
     */
    @Override
    public String pay(PayDTO payDTO) {
        // 直接修改orders记录即可
        // 获取orders记录
        Orders orders = orderMapper.getByNumber(payDTO.getOrderNumber());

        // 直接修改属性
        orders.setStatus(OrderStatusConstant.Pending_Receipt); // 待接单
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayMethod(payDTO.getPayMethod());
        orders.setPayStatus(PayStatusConstant.paid);

        // 修改订单记录
        orderMapper.pay(orders);

        // 向管理端发送信息
        MessageVO messageVO = new MessageVO(WebSocketMessageConstant.PAY, orders.getId(), WebSocketMessageConstant.PAY_CONTENT);
        webSocketServer.sentAll(JSON.toJSONString(messageVO));

        return orders.getEstimatedDeliveryTime().toString();
    }

    /**
     * 用户查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVOForUser getForUser(Long id) {
        // 获取订单记录
        Orders orders = orderMapper.getById(id);

        // 获取订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 创建返回对象
        OrderVOForUser orderVOForUser = new OrderVOForUser();
        // 拷贝属性
        BeanUtils.copyProperties(orders, orderVOForUser);
        orderVOForUser.setOrderDetailList(orderDetailList.toArray(new OrderDetail[]{}));

        return orderVOForUser;
    }

    /**
     * 用户取消订单
     *
     * @param id
     */
    @Override
    public void cancel(Long id) {
        // TODO 用户取消订单，执行退款操作

        // 修改订单状态
        Integer status = OrderStatusConstant.Cancelled;
        LocalDateTime cancelTime = LocalDateTime.now();
        Integer payStatus = PayStatusConstant.refund;
        orderMapper.cancel(id, status, cancelTime, payStatus);
    }

    /**
     * 用户历史订单查询
     *
     * @param userOrderPageQueryDTO
     * @return
     */
    @Override
    public PageQueryVO page(UserOrderPageQueryDTO userOrderPageQueryDTO) {
        PageHelper.startPage(userOrderPageQueryDTO.getPage(), userOrderPageQueryDTO.getPageSize());

        // 获取用户id
        Long userId = BaseContext.getUserId();

        // 执行查询操作
        Page<OrderVOForUser> result = orderMapper.pageForUser(userId, userOrderPageQueryDTO.getStatus());

        // 遍历订单列表获取订单详情
        for (OrderVOForUser orderVOForUser : result) {
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderVOForUser.getId());
            orderVOForUser.setOrderDetailList(orderDetailList.toArray(new OrderDetail[]{}));
        }

        // 创建返回对象
        PageQueryVO pageQueryVO = new PageQueryVO();

        pageQueryVO.setTotal(result.getTotal());
        pageQueryVO.setRecords(result.getResult());
        return pageQueryVO;
    }

    /**
     * 员工查询订单情况
     *
     * @param adminOrderPageQueryDTO
     * @return
     */
    @Override
    public PageQueryVO page(AdminOrderPageQueryDTO adminOrderPageQueryDTO) {
        PageHelper.startPage(adminOrderPageQueryDTO.getPage(), adminOrderPageQueryDTO.getPageSize());
        // 分页查询
        Page<OrderVOForAdmin> orderVOForAdmins = orderMapper.pageForAdmin(adminOrderPageQueryDTO);

        // 对订单记录进行遍历
        for (OrderVOForAdmin orderVOForAdmin : orderVOForAdmins) {
            // 获取订单的菜品id
            List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderVOForAdmin.getId());

            // 转成String
            String[] ids = orderDetails.stream()
                    .map(orderDetail -> orderDetail.getName() + "*" + orderDetail.getNumber())
                    .toArray(String[]::new);

            String orderDishes = String.join(",", ids);

            // 赋值
            orderVOForAdmin.setOrderDishes(orderDishes);
        }

        // 创建返回对象
        PageQueryVO pageQueryVO = new PageQueryVO();

        // 拷贝属性
        pageQueryVO.setRecords(orderVOForAdmins.getResult());
        pageQueryVO.setTotal(orderVOForAdmins.getTotal());

        return pageQueryVO;
    }

    /**
     * 用户催单
     *
     * @param id
     */
    @Override
    public void remind(Long id) {
        // 向管理端发送消息
        MessageVO messageVO = new MessageVO(WebSocketMessageConstant.HURRY, id, WebSocketMessageConstant.HURRY_CONTENT);
        webSocketServer.sentAll(JSON.toJSONString(messageVO));
    }

    /**
     * 员工对各个状态的订单数量统计
     *
     * @return
     */
    @Override
    public StatisticsVO statistics() {
        // 待派送数量
        Integer confirmed = orderMapper.getCountByStatus(OrderStatusConstant.Received);
        // 派送中数量
        Integer deliveryInProgress = orderMapper.getCountByStatus(OrderStatusConstant.Delivery_in_Progress);
        // 待接单数量
        Integer toBeConfirmed = orderMapper.getCountByStatus(OrderStatusConstant.Pending_Receipt);

        // 返回对象
        return new StatisticsVO(confirmed, deliveryInProgress, toBeConfirmed);
    }

    /**
     * 员工查看订单的详细信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderInfoVO getForAdmin(Long id) {
        // 创建对象
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        // 获取订单信息
        Orders orders = orderMapper.getById(id);
        // 拷贝属性
        BeanUtils.copyProperties(orders, orderInfoVO);
        // 获取
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);
        orderInfoVO.setOrderDetailList(orderDetails.toArray(new OrderDetail[]{}));

        // 转成String
        String[] ids = orderDetails.stream()
                .map(orderDetail -> orderDetail.getName() + "*" + orderDetail.getNumber())
                .toArray(String[]::new);

        String orderDishes = String.join(",", ids);

        // 赋值
        orderInfoVO.setOrderDishes(orderDishes);

        return orderInfoVO;
    }

    /**
     * 接单
     *
     * @param id
     */
    @Override
    public void confirm(Long id) {
        // 修改状态即可
        orderMapper.confirm(id, OrderStatusConstant.Received);
    }

    /**
     * 拒单
     *
     * @param rejectDTO
     */
    @Override
    public void reject(RejectDTO rejectDTO) {
        // TODO 商家拒单，退款
        orderMapper.reject(rejectDTO.getId(),rejectDTO.getRejectionReason(), OrderStatusConstant.Refund);
    }

    /**
     * 派送订单
     *
     * @param id
     */
    @Override
    public void delivery(Long id) {
        orderMapper.delivery(id, OrderStatusConstant.Delivery_in_Progress);
    }

    /**
     * 完成订单
     *
     * @param id
     */
    @Override
    public void complete(Long id) {
        orderMapper.complete(id, OrderStatusConstant.Completed);
    }

    /**
     * 因为个人无法申请微信支付，所以返回的信息都是固定的，是写死在这里的
     */
    private OrderSubmitVO pay() {
        BigDecimal orderAmount = new BigDecimal("25.99");
        String orderNumber = UUID.randomUUID().toString();

        // 解析字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String orderTime = formatter.format(LocalDateTime.now());

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(null);
        orderSubmitVO.setOrderNumber(orderNumber);
        orderSubmitVO.setOrderAmount(orderAmount);
        orderSubmitVO.setOrderTime(orderTime);

        return orderSubmitVO;
    }
}
