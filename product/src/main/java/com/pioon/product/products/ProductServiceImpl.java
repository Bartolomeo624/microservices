package com.pioon.product.products;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return saveProduct(product);
    }

    @Override
    @CacheEvict(value = "product", key = "#product.id")
    public Product updateProduct(Product product) {
        if(productRepository.existsById(product.getId())) {
            return saveProduct(product);
        }
        throw new RuntimeException("Product does not exist");
    }

    @Override
    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable("product")
    public Product getProduct(long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            throw new RuntimeException("Product does not exist");
        }
    }

    @Override
    @Cacheable("product")
    public List<Product> getAllProducts() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (List<Product>) productRepository.findAll();
    }


    @Override
    @Cacheable("product")
    public List<Product> findProducts(ProductSearch productSearch) {
        boolean isPriceRangeGiven = (productSearch.getMinPrice() != null
                && productSearch.getMaxPrice() != null);
        boolean isNameGiven = productSearch.getName() != null;

        if (isPriceRangeGiven && isNameGiven) {
            return productRepository.findProductsByPriceBetweenAndNameContainingIgnoreCase(
                    productSearch.getMinPrice(),
                    productSearch.getMaxPrice(),
                    productSearch.getName()
            );
        } else if (isPriceRangeGiven) {
            return productRepository.findProductsByPriceBetween(
                    productSearch.getMinPrice(),
                    productSearch.getMaxPrice()
            );
        } else if (isNameGiven) {
            return productRepository.findProductsByNameContainingIgnoreCase(
                    productSearch.getName()
            );
        } else {
            return getAllProducts();
        }
    }

    @Override
    @Cacheable("product")
    public boolean productExists(List<Long> idList) {
        List<Boolean> objectCheck = new ArrayList<>();

        for (long productId : idList){
            boolean productExists = productRepository.findById(productId).isPresent();

            objectCheck.add(productExists);
        }
        return objectCheck.stream().allMatch(Boolean::booleanValue);
    }

    private Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
