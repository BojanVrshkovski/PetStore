package com.petstore.util;

import com.petstore.entity.enums.PetType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PetConstants {
	public static final Long PET_ID = 1L;
	public static final Long OWNER = 1L;
	public static final String NAME = "Toffi";
	public static final PetType PET_TYPE = PetType.CAT;
	public static final String DESCRIPTION = "The best cat";
	public static final LocalDate DATE_OF_BIRTH = LocalDate.now();
	public static final BigDecimal PRICE = new BigDecimal(200.00);
	public static final Integer RAITING = 5;
}