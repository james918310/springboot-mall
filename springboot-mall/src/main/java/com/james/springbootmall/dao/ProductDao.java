package com.james.springbootmall.dao;

import com.james.springbootmall.constant.ProductCategory;
import com.james.springbootmall.dto.ProductQueryParams;
import com.james.springbootmall.dto.ProductRequest;
import com.james.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

    Integer countProduct(ProductQueryParams productQueryParams);

    void updateStock(Integer productId, Integer stock);
}
