package com.example.sky.constant;

public class OrderStatusConstant {
    // '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款'
    public static final Integer Pending_Payment = 1;
    public static final Integer Pending_Receipt = 2;
    public static final Integer Received = 3;
    public static final Integer Delivery_in_Progress = 4;
    public static final Integer Completed = 5;
    public static final Integer Cancelled = 6;
    public static final Integer Refund = 7;
}
