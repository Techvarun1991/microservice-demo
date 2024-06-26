package com.example.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dto.InventoryResponse;
import com.example.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	 List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
