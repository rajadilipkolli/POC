package com.example.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.domain.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
