package com.example.service;

import com.example.dto.OrderRequest;

public interface OrderService {
	String placeOrder(OrderRequest orderRequest);
}
