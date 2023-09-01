package com.petstore.entity.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserRequest {
	private String firstName;

	private String lastName;

	private String emailAddress;

	private BigDecimal budget;

	public UserRequest() {
	}

	public UserRequest(String firstName, String lastName, String emailAddress, BigDecimal budget) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.budget = budget;
	}
}
