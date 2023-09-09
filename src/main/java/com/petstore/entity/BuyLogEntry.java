package com.petstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "buy_log_entries")
public class BuyLogEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "execution_date")
	private LocalDateTime executionDate;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "allowed_to_buy")
	private boolean allowedToBuy;

	public BuyLogEntry() {
	}

	public BuyLogEntry(Long id, LocalDateTime executionDate, Long userId, boolean allowedToBuy) {
		this.id = id;
		this.executionDate = executionDate;
		this.userId = userId;
		this.allowedToBuy = allowedToBuy;
	}
}
