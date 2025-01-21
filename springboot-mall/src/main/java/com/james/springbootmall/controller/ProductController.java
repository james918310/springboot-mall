package com.james.springbootmall.controller;

import com.james.springbootmall.constant.ProductCategory;
import com.james.springbootmall.dto.ProductQueryParams;
import com.james.springbootmall.dto.ProductRequest;
import com.james.springbootmall.model.Product;
import com.james.springbootmall.service.ProductService;
import com.james.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //    查詢全部資料
    @GetMapping("")
    public ResponseEntity<Page<Product>> getAllProducts(
//            查詢條件
            @RequestParam(required = false) ProductCategory category,  //資料種類
            @RequestParam(required = false) String search,   //收尋查詢
            @RequestParam(required = false) String minPrice, //價格區間最小值
            @RequestParam(required = false) String maxPrice, //價格區間最大值
            //排序
            @RequestParam(defaultValue = "created_date") String orderBy, //依據哪個欄位排序
            @RequestParam(defaultValue = "desc") String sort,
            //頁面
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit, //一次取幾筆資料
            @RequestParam(defaultValue = "0") @Min(0) Integer offset            //一次要跳過多少筆資料

    ) {

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);
        productQueryParams.setMinPrice(minPrice);
        productQueryParams.setMaxPrice(maxPrice);

        List<Product> productList = productService.getProducts(productQueryParams);

        Integer total = productService.countProduct(productQueryParams);

        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);


        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    //    查詢資料
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        productService.updateProduct(productId, productRequest);
        Product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
