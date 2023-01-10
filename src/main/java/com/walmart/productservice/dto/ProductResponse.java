package com.walmart.productservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Comparable<ProductResponse> {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    
    @Override
    public int compareTo(ProductResponse o) {
        return this.price.compareTo(o.price);
    }
}
