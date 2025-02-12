package com.james.springbootmall.dto.shoppingCar;

import java.util.List;

public class CartDTO {
    private List<CartItemDTO> buyItemList; // 與前端 JSON 格式對應

    // 無參數建構子
    public CartDTO() {
    }

    // 有參數建構子
    public CartDTO(List<CartItemDTO> buyItemList) {
        this.buyItemList = buyItemList;
    }

    // Getter 和 Setter 方法
    public List<CartItemDTO> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<CartItemDTO> buyItemList) {
        this.buyItemList = buyItemList;
    }
}

