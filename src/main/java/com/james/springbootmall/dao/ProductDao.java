package com.james.springbootmall.dao;

import com.james.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}
