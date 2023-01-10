package com.walmart.productservice.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.walmart.productservice.dto.ProductRequest;
import com.walmart.productservice.dto.ProductResponse;
import com.walmart.productservice.model.ProductEntity;
import com.walmart.productservice.repository.IProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final IProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        ProductEntity productEntity = ProductEntity.builder()
                            .name(productRequest.getName())
                            .description(productRequest.getDescription())
                            .price(productRequest.getPrice()).build();
        productRepository.save(productEntity);
        log.info("Product {} is saved", productEntity.getId());
    }

    public void saveProductList(List<ProductRequest> productRequestList) {
        List<ProductEntity> productEntityList = productRequestList.stream().map(this::mapRequestToEntity).toList();
        productRepository.saveAll(productEntityList);
    }

    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> productEntityList =  productRepository.findAll();
        //productEntityList.stream().map(product -> mapToProductResponse(product));
        return productEntityList.stream().map(this::mapToProductResponse).toList();
    }

    public void deleteProduct() {
        productRepository.deleteAll();    
    }

    public List<ProductResponse> getProductsByLowPrice() {
                return productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"))
                                        .stream()
                                        .map(this::mapToProductResponse).toList();
    }

    private ProductEntity mapRequestToEntity(ProductRequest productRequest) {
        return ProductEntity.builder()
                            .name(productRequest.getName())
                            .description(productRequest.getDescription())
                            .price(productRequest.getPrice()).build();
    }

    private ProductResponse mapToProductResponse(ProductEntity productEntity) {
        return ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice()).build();
    }
}