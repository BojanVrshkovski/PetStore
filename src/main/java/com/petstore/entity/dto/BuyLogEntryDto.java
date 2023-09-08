package com.petstore.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyLogEntryDto {

	private Long id;

	private LocalDateTime executionDate;

	private Long userId;

	private boolean allowedToBuy;

	public BuyLogEntryDto() {
	}

	public BuyLogEntryDto(Long id, LocalDateTime executionDate, Long userId, boolean allowedToBuy) {
		this.id = id;
		this.executionDate = executionDate;
		this.userId = userId;
		this.allowedToBuy = allowedToBuy;
	}
}
