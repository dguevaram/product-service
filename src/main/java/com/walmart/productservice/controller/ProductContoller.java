package com.walmart.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.productservice.dto.ProductRequest;
import com.walmart.productservice.dto.ProductResponse;
import com.walmart.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductContoller {
    
    private final ProductService productService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @PostMapping("/create/list")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProductList(@RequestBody List<ProductRequest> productRequestList) {
        productService.saveProductList(productRequestList);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
       return productService.getAllProducts();
    }

    @GetMapping("/low/price")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProductsByLowPrice(){
        return productService.getProductsByLowPrice();
    }
}
