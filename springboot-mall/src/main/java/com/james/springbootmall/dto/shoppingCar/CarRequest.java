package com.james.springbootmall.dto.shoppingCar;

import com.james.springbootmall.model.Product;


//我需要 product，數量，單項目價格， 小計，總價格
public class CarRequest {

    private Product product;  //商品資料

    private Integer quantity;  //數量
    private Integer amount; //單品價格


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
