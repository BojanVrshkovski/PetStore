package com.petstore.entity.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserRequest {
	private String firstName;

	private String lastName;

	private String emailAddress;

	private BigDecimal budget;
}
