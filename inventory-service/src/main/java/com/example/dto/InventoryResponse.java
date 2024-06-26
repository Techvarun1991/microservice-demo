package com.example.dto;


public class InventoryResponse {
	private String skuCode;
	private boolean inStock;
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public boolean isInStock() {
		return inStock;
	}
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	@Override
	public String toString() {
		return "InventoryResponse [skuCode=" + skuCode + ", inStock=" + inStock + "]";
	}
	public InventoryResponse(String skuCode, boolean inStock) {
		super();
		this.skuCode = skuCode;
		this.inStock = inStock;
	}
	public InventoryResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
