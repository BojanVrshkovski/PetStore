package com.petstore.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {
	private String firstName;

	private String lastName;

	private String emailAddress;

	private BigDecimal budget;
}
