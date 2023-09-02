package com.petstore.controller;

import com.petstore.entity.Pet;
import com.petstore.entity.request.PetRequest;
import com.petstore.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PetController {
	private final PetService petService;

	@Autowired
	public PetController(PetService petService) {
		this.petService = petService;
	}

	@MutationMapping
	public Pet createPet(PetRequest petRequest){
		return petService.createPet(petRequest);
	}
}
