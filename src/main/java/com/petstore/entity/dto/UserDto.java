package com.petstore.entity.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserDto {
	private Long userId;
	private String firstName;

	private String lastName;

	private String emailAddress;

	private BigDecimal budget;

	public UserDto() {
	}

	public UserDto(Long userId, String firstName, String lastName, String emailAddress, BigDecimal budget) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.budget = budget;
	}
}
