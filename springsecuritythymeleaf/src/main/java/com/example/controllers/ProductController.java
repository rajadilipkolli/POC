package com.example.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.domain.Product;
import com.example.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("product/new")
	public String newProduct(Model model) {
		model.addAttribute("product", new Product());
		return "productform";
	}

	@Secured(value = { "ROLE_ADMIN" })
	@PostMapping(value = "product")
	public String saveProduct(Product product) {
		productService.saveProduct(product);
		return "redirect:/product/" + product.getId();
	}

	// Read
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("product/{id}")
	public String showProduct(@PathVariable String id, Model model) {
		model.addAttribute("product", productService.getProductById(id));
		return "productshow";
	}

	@GetMapping(value = "/products")
	public String list(Model model) {
		model.addAttribute("products", productService.listAllProducts());
		return "products";
	}

	// Update
	@GetMapping("product/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		model.addAttribute("product", productService.getProductById(id));
		return "productform";
	}

	// Delete
	@GetMapping("product/delete/{id}")
	public String delete(@PathVariable String id) {
		productService.deleteProduct(id);
		return "redirect:/products";
	}
}
