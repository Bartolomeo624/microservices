package com.pioon.product.products;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSearch {
    private Float minPrice;
    private Float maxPrice;
    private String name;
}
