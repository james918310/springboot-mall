package com.james.springbootmall.controller;

import com.james.springbootmall.dto.CreateOrderRequest;
import com.james.springbootmall.dto.OrderQueryParams;
import com.james.springbootmall.dto.shoppingCar.CarRequest;
import com.james.springbootmall.dto.shoppingCar.CartDTO;
import com.james.springbootmall.model.Order;
import com.james.springbootmall.service.OrderService;
import com.james.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    //    創建購物車目錄品項
    @PostMapping("/users/{userId}/shoppingCar")
    public ResponseEntity<List<CarRequest>> receiveCart(@PathVariable Integer userId,
                                                        @RequestBody @Valid CartDTO cartDTO) {

        List<CarRequest> listCarRequest = orderService.shoppingCar(userId, cartDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(listCarRequest);

    }


    //    下訂單
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        // 取得 order list
        List<Order> orderList = orderService.getOrders(orderQueryParams);

        // 取得 order 總數
        Integer count = orderService.countOrders(orderQueryParams);

        // 分頁
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


}
