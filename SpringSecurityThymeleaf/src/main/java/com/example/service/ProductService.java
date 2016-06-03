package com.example.service;

import java.util.List;

import com.example.domain.Product;

public interface ProductService {

    Product saveProduct(Product product);

    Product getProductById(String id);

    List<Product> listAllProducts();
    
    void deleteProduct(String id);

}
