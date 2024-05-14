package com.pioon.product.products;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findProductsByPriceBetween(float minPrice, float maxPrince);
    List<Product> findProductsByNameContainingIgnoreCase(String name);
    List<Product> findProductsByPriceBetweenAndNameContainingIgnoreCase(float minPrice, float maxPrince, String name);

}
