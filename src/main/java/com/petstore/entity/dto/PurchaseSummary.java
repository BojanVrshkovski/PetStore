package com.petstore.entity.dto;

import lombok.Data;

@Data
public class PurchaseSummary {
	private int successfulPurchases;
	private int failedPurchases;

	public PurchaseSummary() {
	}

	public PurchaseSummary(int successfulPurchases, int failedPurchases) {
		this.successfulPurchases = successfulPurchases;
		this.failedPurchases = failedPurchases;
	}
}
