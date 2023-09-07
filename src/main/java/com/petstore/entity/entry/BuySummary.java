package com.petstore.entity.entry;

import lombok.Data;

@Data
public class BuySummary {
	private int successfulPurchases;
	private int failedPurchases;

	public BuySummary() {
	}

	public BuySummary(int successfulPurchases, int failedPurchases) {
		this.successfulPurchases = successfulPurchases;
		this.failedPurchases = failedPurchases;
	}
}
