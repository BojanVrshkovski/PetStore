package com.petstore.service;

import com.petstore.entity.Pet;
import com.petstore.entity.request.PetRequest;

public interface PetService {
	Pet createPet(PetRequest petRequest);
}
