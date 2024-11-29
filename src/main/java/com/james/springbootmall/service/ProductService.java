package com.james.springbootmall.service;

import com.james.springbootmall.dto.ProductRequest;
import com.james.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);


}
