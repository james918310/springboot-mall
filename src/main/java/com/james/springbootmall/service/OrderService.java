package com.james.springbootmall.service;

import com.james.springbootmall.dto.CreateOrderRequest;
import com.james.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);
}
