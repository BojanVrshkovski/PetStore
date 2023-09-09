package com.petstore.util;

import com.petstore.entity.Pet;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

	public static Pet getDefaultPet(){
		Pet pet = new Pet(PET_ID, OWNER, NAME,PET_TYPE,DESCRIPTION,DATE_OF_BIRTH,PRICE,RAITING);

		return pet;
	}

	public static PetRequest getDefaultPetRequest(){
		PetRequest petRequest = new PetRequest(OWNER, NAME,PET_TYPE,DESCRIPTION,DATE_OF_BIRTH,PRICE,RAITING);

		return petRequest;
	}

	public static List<Pet> createDummyPets(int count) {
		List<Pet> pets = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			Pet pet = new Pet();
			pet.setPetId((long) i);
			pet.setOwner((long) i);
			pet.setName("Toffi");
			pet.setPetType(PetType.CAT);
			pet.setDescription("The best cat");
			pet.setPrice(new BigDecimal(100.00));
			pet.setRating(10);

			pets.add(pet);
		}
		return pets;
	}
}
