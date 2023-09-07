package com.petstore.controller;

import com.petstore.entity.Pet;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.dto.PurchaseSummary;
import com.petstore.entity.request.PetRequest;
import com.petstore.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PetController {
	private final PetService petService;

	@Autowired
	public PetController(PetService petService) {
		this.petService = petService;
	}

	@MutationMapping
	public Pet createPet(@Argument PetRequest petRequest){
		return petService.createPet(petRequest);
	}

	@QueryMapping
	public List<PetDto> readAllPets(){
		return petService.readAllPets();
	}

	@MutationMapping
	public Pet buy(@Argument Long userId,@Argument Long petId){
		return petService.buy(userId,petId);
	}

	@MutationMapping
	public PurchaseSummary buyAll(){
		return petService.buyAll();
	}
	@QueryMapping
	public PetDto readPetById(@Argument Long petId) {
		return petService.readPetById(petId);
	}
	@MutationMapping
	public List<Pet> createRandomPets(@Argument int count){
		return petService.createRandomPets(count);
	}
}
