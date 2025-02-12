package com.james.springbootmall.dto.shoppingCar;

public class CartItemDTO {
    private Integer productId;
    private Integer quantity;

    // 無參數建構子（必要）
    public CartItemDTO() {
    }

    // 有參數建構子（方便創建）
    public CartItemDTO(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getter 和 Setter 方法
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
