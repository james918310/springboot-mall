package com.james.springbootmall.service;

import com.james.springbootmall.dto.CreateOrderRequest;
import com.james.springbootmall.dto.OrderQueryParams;
import com.james.springbootmall.dto.shoppingCar.CarRequest;
import com.james.springbootmall.dto.shoppingCar.CartDTO;
import com.james.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrders(OrderQueryParams orderQueryParams);

    List<CarRequest> shoppingCar(Integer userId, CartDTO cartDTO);
}
