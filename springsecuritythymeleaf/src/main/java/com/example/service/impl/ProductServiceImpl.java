package com.example.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.domain.Product;
import com.example.repositories.ProductRepository;
import com.example.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Optional<Product> getProductById(String id) {
		return productRepository.findById(id);
	}

	@Override
	public List<Product> listAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public void deleteProduct(String id) {
		productRepository.deleteById(id);
	}

}
