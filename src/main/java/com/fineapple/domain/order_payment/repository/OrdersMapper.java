package com.fineapple.domain.order_payment.repository;

import com.fineapple.domain.order_payment.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {

    Orders selectOrderById(Long id);

    void insertOrder(Orders orders);
}
