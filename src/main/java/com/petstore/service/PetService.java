package com.petstore.service;

import com.petstore.entity.Pet;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.entry.PurchaseSummary;
import com.petstore.entity.request.PetRequest;

import java.util.List;

public interface PetService {
	Pet createPet(PetRequest petRequest);
	List<PetDto> readAllPets();
	Pet buy(Long userId,Long petId);
	PetDto readPetById(Long petId);
	PurchaseSummary buyAll();
}
