package com.walmart.productservice.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.walmart.productservice.model.ProductEntity;

public interface IProductRepository extends MongoRepository<ProductEntity, String> {
    public abstract List<ProductEntity> findByName(String name);
    public abstract List<ProductEntity> findByPrice(BigDecimal price);
    public abstract List<ProductEntity> findByDescription(String description);
}
