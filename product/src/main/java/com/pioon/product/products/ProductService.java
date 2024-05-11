package com.pioon.product.products;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(long id);
    Product getProduct(long id);

    List<Product> getAllProducts();
    List<Product> findProducts(ProductSearch productSearch);
}
