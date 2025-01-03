package com.james.springbootmall.service;

import com.james.springbootmall.constant.ProductCategory;
import com.james.springbootmall.dto.ProductQueryParams;
import com.james.springbootmall.dto.ProductRequest;
import com.james.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

    Integer countProduct(ProductQueryParams productQueryParams);


}
