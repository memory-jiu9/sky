package com.example.sky.service.impl;

import com.example.sky.constant.OrderStatusConstant;
import com.example.sky.constant.PayStatusConstant;
import com.example.sky.context.BaseContext;
import com.example.sky.mapper.AddressBookMapper;
import com.example.sky.mapper.OrderDetailMapper;
import com.example.sky.mapper.OrderMapper;
import com.example.sky.mapper.ShoppingCartMapper;
import com.example.sky.pojo.dto.OrderAddDTO;
import com.example.sky.pojo.dto.OrderPageQueryDTO;
import com.example.sky.pojo.dto.PayDTO;
import com.example.sky.pojo.entity.AddressBook;
import com.example.sky.pojo.entity.OrderDetail;
import com.example.sky.pojo.entity.Orders;
import com.example.sky.pojo.entity.ShoppingCart;
import com.example.sky.pojo.vo.OrderSubmitVO;
import com.example.sky.pojo.vo.OrderVO;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.service.OrderService;
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

        // 设置属性
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

        // 修改属性
        orders.setStatus(OrderStatusConstant.Pending_Receipt); // 待接单
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayMethod(payDTO.getPayMethod());
        orders.setPayStatus(PayStatusConstant.paid);

        // 修改订单记录
        orderMapper.pay(orders);

        return orders.getEstimatedDeliveryTime().toString();
    }

    /**
     * 用户查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO get(Long id) {
        // 获取订单记录
        Orders orders = orderMapper.getById(id);

        // 获取订单详情
        OrderDetail[] orderDetailList = orderDetailMapper.getByOrderId(id);

        // 创建返回对象
        OrderVO orderVO = new OrderVO();
        // 拷贝属性
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 用户取消订单
     *
     * @param id
     */
    @Override
    public void cancel(Long id) {
        // TODO 退款操作，可能涉及查询操作

        // 修改订单状态
        Integer status = OrderStatusConstant.Cancelled;
        LocalDateTime cancelTime = LocalDateTime.now();
        Integer payStatus = PayStatusConstant.refund;
        orderMapper.cancel(id, status, cancelTime, payStatus);
    }

    /**
     * 用户历史订单查询
     *
     * @param orderPageQueryDTO
     * @return
     */
    @Override
    public PageQueryVO page(OrderPageQueryDTO orderPageQueryDTO) {
        PageHelper.startPage(orderPageQueryDTO.getPage(), orderPageQueryDTO.getPageSize());

        // 获取用户id
        Long userId = BaseContext.getUserId();

        // 执行查询操作
        Page<OrderVO> result = orderMapper.page(userId, orderPageQueryDTO.getStatus());

        // 遍历订单列表获取订单详情
        for (OrderVO orderVO : result) {
            OrderDetail[] orderDetailList = orderDetailMapper.getByOrderId(orderVO.getId());
            orderVO.setOrderDetailList(orderDetailList);
        }

        // 创建返回对象
        PageQueryVO pageQueryVO = new PageQueryVO();

        pageQueryVO.setTotal(result.getTotal());
        pageQueryVO.setRecords(result.getResult());
        return pageQueryVO;
    }

    /**
     * 用户催单
     * @param id
     */
    @Override
    public void remind(Long id) {
        // TODO 通过websocket向管理端发送消息
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
