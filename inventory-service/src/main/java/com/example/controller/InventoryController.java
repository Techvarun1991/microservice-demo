package com.example.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.InventoryResponse;
import com.example.service.InventoryService;

import jakarta.ws.rs.GET;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping("/working")
	@ResponseStatus(value = HttpStatus.OK)
	public String working() {
		return "Working";
	}
	
	@GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
	
	
	@GetMapping("/admin")
	//ONLY ADMIN
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(value = HttpStatus.OK)
	public String admin() {
		return "Working";
	}
	
	@GetMapping("/user")
	//ONLY USER
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@ResponseStatus(value = HttpStatus.OK)
	public String user() {
		return "Working";
	}
	
	@GetMapping("/common")
	//USER AND ADMIN BOTH
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(value = HttpStatus.OK)
	public String common() {
		return "Working";
	}
	
	
	
}
