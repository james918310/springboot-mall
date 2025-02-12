package com.james.springbootmall.service.impl;

import com.james.springbootmall.dao.OrderDao;
import com.james.springbootmall.dao.ProductDao;
import com.james.springbootmall.dao.UserDao;
import com.james.springbootmall.dto.BuyItem;
import com.james.springbootmall.dto.CreateOrderRequest;
import com.james.springbootmall.dto.OrderQueryParams;
import com.james.springbootmall.dto.shoppingCar.CarRequest;
import com.james.springbootmall.dto.shoppingCar.CartDTO;
import com.james.springbootmall.dto.shoppingCar.CartItemDTO;
import com.james.springbootmall.model.Order;
import com.james.springbootmall.model.OrderItem;
import com.james.springbootmall.model.Product;
import com.james.springbootmall.model.User;
import com.james.springbootmall.service.OrderService;
import com.james.springbootmall.service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemById(orderId);

        order.setOrderItemsList(orderItemList);

        return order;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemById(order.getOrderId());

            order.setOrderItemsList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Integer countOrders(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }


    @Transactional  //在多個table中操作時加入這個可以增加資料的一致性
    @Override   //下單
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //檢查是否有user存在
        User user = userDao.getUserById(userId);
        if (user == null) {
            log.warn("該userId{}不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());
//            檢查商品是否存在與庫存是否足夠
            if (product == null) {
                log.warn("商品{}不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品{}庫存不足，無法購買", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
//            扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());
//        計算總價格
            int amount = product.getPrice() * buyItem.getQuantity();
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        //創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        //訂單創建完成，寄信給使用者

        String email = createOrderRequest.getEmail();
        emailService.createOrder(email, "下單成功", "恭喜你在Book Cart成功下創建了一筆新的訂單，訂單編號是 : " + orderId);


        return orderId;
    }

    @Override
    public List<CarRequest> shoppingCar(Integer userId, CartDTO cartDTO) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            log.warn("該userId{}不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<CarRequest> orderItemList = new ArrayList<>();

        for (CartItemDTO cartItemDTO : cartDTO.getBuyItemList()) {
            Product product = productDao.getProductById(cartItemDTO.getProductId());
//            檢查商品是否存在與庫存是否足夠
            if (product == null) {
                log.warn("商品{}不存在", cartItemDTO.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < cartItemDTO.getQuantity()) {
                log.warn("商品{}庫存不足，無法購買", cartItemDTO.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

//        計算總價格
            int amount = product.getPrice() * cartItemDTO.getQuantity();


//            我需要 product，數量，單項目價格， 小計，總價格

            CarRequest carRequest = new CarRequest();
            carRequest.setProduct(product);
            carRequest.setQuantity(cartItemDTO.getQuantity());
            carRequest.setAmount(amount);

            orderItemList.add(carRequest);
        }


        return orderItemList;
    }
}


