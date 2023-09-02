package com.petstore.service.impl;

import com.petstore.entity.Pet;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.PetAlredyExistsException;
import com.petstore.exception.UserAlreadyExistException;
import com.petstore.repository.PetRepository;
import com.petstore.service.PetService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PetServiceImpl implements PetService {
	private static final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);
	private final PetRepository petRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public PetServiceImpl(PetRepository petRepository, ModelMapper modelMapper) {
		this.petRepository = petRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Pet createPet(PetRequest petRequest) {
		if (petRequest == null) {
			throw new IllegalArgumentException("petRequest is null");
		}
		Pet pet;
		try {
			pet = modelMapper.map(petRequest, Pet.class);

			if (petRequest.getPetType().equals(PetType.CAT)){
				LocalDate birthDate = petRequest.getDateOfBirth();
				LocalDate currentDate = LocalDate.now();
				long ageInYears = ChronoUnit.YEARS.between(birthDate, currentDate);
				BigDecimal price = BigDecimal.valueOf(ageInYears).multiply(BigDecimal.ONE);
				pet.setPrice(price);

			}else {
				LocalDate birthDate = petRequest.getDateOfBirth();
				LocalDate currentDate = LocalDate.now();
				long ageInYears = ChronoUnit.YEARS.between(birthDate, currentDate);
				int rating = petRequest.getRating();
				BigDecimal price = BigDecimal.valueOf(ageInYears).multiply(BigDecimal.ONE).add(BigDecimal.valueOf(rating).multiply(BigDecimal.ONE));
				pet.setPrice(price);
			}

			pet = petRepository.save(pet);
			log.info(String.format("Pet successfully added in database with name: %s",petRequest.getName()));
		}catch (DataIntegrityViolationException e){
			log.error(String.format("The pet alredy exists"));
			throw new PetAlredyExistsException("The pet alredy exists");
		}
		return pet;
	}
}
