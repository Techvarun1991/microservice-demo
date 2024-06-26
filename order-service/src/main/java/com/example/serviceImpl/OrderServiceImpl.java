package com.example.serviceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.dto.InventoryResponse;
import com.example.dto.OrderLineItemsDto;
import com.example.dto.OrderRequest;
import com.example.model.Orders;
import com.example.model.OrderLineItems;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
//	@Autowired
//	private WebClient webClient;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	public String placeOrder(OrderRequest orderRequest) {
		Orders order = new Orders();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
	                .stream()
	                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
	                .toList();

	    order.setOrderLineItemsList(orderLineItems);
	    
	    
	    List<String> skuCodes = order.getOrderLineItemsList().stream().map(orderLineItem -> orderLineItem.getSkuCode()).toList();
	    // call inventory service to check product is in stock or not
	    InventoryResponse[] inventoryResponsArray = webClientBuilder.build().get()
                .uri("http://inventory-service/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
	     
	    boolean allProductsInStock = Arrays.stream(inventoryResponsArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());
	    
	    if(allProductsInStock) {
	    	orderRepository.save(order);
	    	return "Order Placed Successfully !!";
	    }else {
	    	throw new IllegalArgumentException("Product is not in stock, please try again later");
	    }
	    
	}

	 private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
	        OrderLineItems orderLineItems = new OrderLineItems();
	        orderLineItems.setPrice(orderLineItemsDto.getPrice());
	        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
	        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
	        return orderLineItems;
	 }
	 
	

//	    public InventoryResponse[] getInventory(List<String> skuCodes) {
//	    	Object[] skuCodesArray = skuCodes.toArray();
//	        return webClient.get()
//	                .uri("http://inventory-service/inventory",
//	                        uriBuilder -> uriBuilder.queryParam("skuCode", (Object[]) skuCodesArray).build())
//	                .retrieve()
//	                .bodyToMono(InventoryResponse[].class)
//	                .block();
//	    }
}
