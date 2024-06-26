package com.example.service;

import java.util.List;

import com.example.model.Product;

public interface ProductService {

	void createProduct(Product product);
	List<Product> getAll();

}
