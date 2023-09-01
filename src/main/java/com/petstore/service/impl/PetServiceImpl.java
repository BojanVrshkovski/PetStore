package com.petstore.service.impl;

import com.petstore.repository.PetRepository;
import com.petstore.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {
	private final PetRepository petRepository;

	@Autowired
	public PetServiceImpl(PetRepository petRepository) {
		this.petRepository = petRepository;
	}
}
