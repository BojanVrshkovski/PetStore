package com.petstore.util;

import com.petstore.entity.dto.PetDto;

import static com.petstore.util.PetConstants.PET_ID;
import static com.petstore.util.PetConstants.OWNER;
import static com.petstore.util.PetConstants.NAME;
import static com.petstore.util.PetConstants.PET_TYPE;
import static com.petstore.util.PetConstants.DESCRIPTION;
import static com.petstore.util.PetConstants.DATE_OF_BIRTH;
import static com.petstore.util.PetConstants.PRICE;
import static com.petstore.util.PetConstants.RAITING;

public class PetFactory {

	public static PetDto getDefaultPetDto(){
		PetDto petDto = new PetDto(PET_ID, OWNER, NAME,PET_TYPE,DESCRIPTION,DATE_OF_BIRTH,PRICE,RAITING);

		return petDto;
	}
}
