package com.example.service;

import java.util.List;

import com.example.dto.InventoryResponse;

public interface InventoryService {
	List<InventoryResponse> isInStock(List<String> skuCode);
}
