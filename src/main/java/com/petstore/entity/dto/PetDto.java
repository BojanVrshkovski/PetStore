package com.petstore.entity.dto;

import com.petstore.entity.enums.PetType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PetDto {
	private Long petId;
	private Long owner;

	private String name;

	private PetType petType;

	private String description;

	private LocalDate dateOfBirth;

	private BigDecimal price;

	private Integer rating;

	public PetDto() {
	}

	public PetDto(
		Long petId, Long owner, String name, PetType petType, String description, LocalDate dateOfBirth, BigDecimal price,
		Integer rating) {
		this.petId = petId;
		this.owner = owner;
		this.name = name;
		this.petType = petType;
		this.description = description;
		this.dateOfBirth = dateOfBirth;
		this.price = price;
		this.rating = rating;
	}
}
