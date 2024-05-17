package com.pioon.product.products;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findProductsByProductPriceBetween(float minPrice, float maxPrince);
    List<Product> findProductsByProductNameContainingIgnoreCase(String name);
    List<Product> findProductsByProductPriceBetweenAndProductNameContainingIgnoreCase(float minPrice, float maxPrince, String name);

}
