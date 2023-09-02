package com.petstore.entity.request;

import com.petstore.entity.User;
import com.petstore.entity.enums.PetType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PetRequest {
	private Long owner;

	private String name;

	private PetType petType;

	private String description;

	private LocalDate dateOfBirth;

	private BigDecimal price;

	private Integer rating;

	public PetRequest() {
	}

	public PetRequest(
		Long owner, String name, PetType petType, String description, LocalDate dateOfBirth, BigDecimal price,
		Integer rating) {
		this.owner = owner;
		this.name = name;
		this.petType = petType;
		this.description = description;
		this.dateOfBirth = dateOfBirth;
		this.price = price;
		this.rating = rating;
	}
}
