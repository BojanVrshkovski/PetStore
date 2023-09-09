package com.petstore.util;

import com.petstore.entity.enums.PetType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PetConstants {
	public static final Long petId = 1L;
	public static final Long owner = 2L;
	public static final String name = "Toffi";
	public static final PetType petType = PetType.CAT;
	public static final String description = "The best cat";
	public static final LocalDate dateOfBirth = LocalDate.now();
	public static final BigDecimal price = new BigDecimal(200.00);
	public static final Integer raiting = 5;
}