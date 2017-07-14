package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.domain.Product;

public interface ProductService {

    Product saveProduct(Product product);

    Optional<Product> getProductById(String id);

    List<Product> listAllProducts();
    
    void deleteProduct(String id);

}
