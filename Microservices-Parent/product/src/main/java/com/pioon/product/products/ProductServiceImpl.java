package com.pioon.product.products;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Product updateProduct(Product product) {
        if(productRepository.existsById(product.getProductId())) {
            saveProduct(product);
        }
        throw new RuntimeException("Product does not exist");
    }

    @Override
    public void deleteProduct(long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    @Override
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
    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }


    @Override
    public List<Product> findProducts(ProductSearch productSearch) {
        boolean isPriceRangeGiven = (productSearch.getMinPrice() != null
                && productSearch.getMaxPrice() != null);
        boolean isNameGiven = productSearch.getName() != null;

        if (isPriceRangeGiven && isNameGiven) {
            return productRepository.findProductsByProductPriceBetweenAndProductNameContainingIgnoreCase(
                    productSearch.getMinPrice(),
                    productSearch.getMaxPrice(),
                    productSearch.getName()
            );
        } else if (isPriceRangeGiven) {
            return productRepository.findProductsByProductPriceBetween(
                    productSearch.getMinPrice(),
                    productSearch.getMaxPrice()
            );
        } else if (isNameGiven) {
            return productRepository.findProductsByProductNameContainingIgnoreCase(
                    productSearch.getName()
            );
        } else {
            return getAllProducts();
        }
    }

    @Override
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
