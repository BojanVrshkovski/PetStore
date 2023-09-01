package com.petstore.entity.request;

import com.petstore.entity.User;
import com.petstore.entity.enums.PetType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PetRequest {
	private User owner;

	private String name;

	private PetType petType;

	private String description;

	private LocalDate dateOfBirth;

	private BigDecimal price;

	private Integer rating;
}
