package com.example.sky.mapper;

import com.example.sky.pojo.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 添加订单详情
     * @param orderDetails
     */
    void batchAdd(ArrayList<OrderDetail> orderDetails);

    /**
     * 根据订单id获取记录
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);
}
