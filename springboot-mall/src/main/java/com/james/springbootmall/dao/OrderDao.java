package com.james.springbootmall.dao;

import com.james.springbootmall.dto.OrderQueryParams;
import com.james.springbootmall.model.Order;
import com.james.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

    List<OrderItem> getOrderItemById(Integer orderId);

    Order getOrderById(Integer orderId);

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);


}
