package com.petstore.service.impl;

import com.github.javafaker.Faker;
import com.petstore.entity.Pet;
import com.petstore.entity.User;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.dto.PurchaseSummary;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.NoPetsFoundException;
import com.petstore.exception.NotEnoughBudgetException;
import com.petstore.exception.PetAlredyExistsException;
import com.petstore.exception.PetAlredyHasOwnerException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.repository.PetRepository;
import com.petstore.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {
	private static final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);
	private final PetRepository petRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public PetServiceImpl(PetRepository petRepository, UserRepository userRepository, ModelMapper modelMapper) {
		this.petRepository = petRepository;
		this.userRepository = userRepository;
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

	@Override
	public List<PetDto> readAllPets() {
		List<Pet> pets = petRepository.findAll();
		if (pets.isEmpty()) {
			log.error(String.format("No pets found"));
			throw new NoPetsFoundException("No pets found");
		}
		log.info(String.format("Retriving all the pets"));
		return pets.stream()
		            .map(pet -> modelMapper.map(pet, PetDto.class))
		            .collect(Collectors.toList());
	}

	@Override
	public Pet buy(Long userId, Long petId) {
		Optional<User> userOptional = userRepository.findById(userId);
		Optional<Pet> petOptional = petRepository.findById(petId);

		if (userOptional.isEmpty()) {
			log.error(String.format("The user you provided is not found"));
			throw new UserNotFoundException();
		}else if (petOptional.isEmpty()){
			log.error(String.format("Pet not found"));
			throw new PetNotFoundException();
		}

		User user = userOptional.get();
		Pet pet = petOptional.get();

		if (pet.getOwner() != null) {
			log.error(String.format("Pet already has an owner"));
			throw new PetAlredyHasOwnerException("Pet already has an owner");
		}

		BigDecimal userBudget = user.getBudget();
		BigDecimal petPrice = pet.getPrice();

		if (userBudget.compareTo(petPrice) < 0) {
			log.error(String.format("The user with name %s has not enough budget",user.getFirstName()));
			throw new NotEnoughBudgetException("Not enough budget");
		}

		user.setBudget(userBudget.subtract(petPrice));

		pet.setOwner(user.getUserId());

		userRepository.save(user);
		petRepository.save(pet);

		if (pet.getPetType() == PetType.CAT) {
			log.info(String.format("The cat is successfully bought"));
			System.out.println("Meow, cat " + pet.getName() + " has owner " + user.getFirstName());
		} else if (pet.getPetType() == PetType.DOG) {
			log.info(String.format("The dog is successfully bought\""));
			System.out.println("Woof, dog " + pet.getName() + " has owner " + user.getFirstName());
		}

		log.info(String.format("Purchase successful"));
		return pet;
	}

	public PurchaseSummary buyAll() {
		List<User> users = userRepository.findAll();
		List<Pet> availablePets = petRepository.findAvailablePets();

		int successfulPurchases = 0;
		int failedPurchases = 0;

		for (User user : users) {
			for (Pet pet : availablePets) {
				try {
					buy(user.getUserId(), pet.getPetId());
					successfulPurchases++;
				} catch (Exception e) {
					log.error(String.format("User %s could not make a purchase: %s", user.getFirstName(), e.getMessage()));
					failedPurchases++;
				}
			}
		}

		PurchaseSummary purchaseSummary = new PurchaseSummary();
		purchaseSummary.setSuccessfulPurchases(successfulPurchases);
		purchaseSummary.setFailedPurchases(failedPurchases);
		return purchaseSummary;
	}

	@Override
	public PetDto readPetById(Long petId) {
		log.info(String.format("Pet with id: %d is being fetched", petId));
		Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
		return modelMapper.map(pet, PetDto.class);
	}

	@Override
	public List<Pet> createRandomPets(int count) {
		if (count <= 0 || count > 20) {
			throw new IllegalArgumentException("Count should be between 1 and 20");
		}

		List<Pet> createdPets = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			PetRequest randomPetRequest = generateRandomPetData();
			Pet createdPet = createPet(randomPetRequest);
			createdPets.add(createdPet);
		}

		return createdPets;
	}

	private PetRequest generateRandomPetData(){
		Faker faker = new Faker();

		PetRequest petRequest = new PetRequest();
		petRequest.setName(faker.animal().name());
		petRequest.setRating(faker.number().numberBetween(1,10));
		petRequest.setPetType(Math.random() < 0.5 ? PetType.CAT : PetType.DOG);
		petRequest.setDescription(faker.lorem().sentence());
		
		LocalDate currentDate = LocalDate.now();
		LocalDate minBirthDate = currentDate.minusYears(15);
		LocalDate maxBirthDate = currentDate.minusYears(5);
		long minDay = minBirthDate.toEpochDay();
		long maxDay = maxBirthDate.toEpochDay();
		long randomDay = minDay + (long) (Math.random() * (maxDay - minDay));
		petRequest.setDateOfBirth(LocalDate.ofEpochDay(randomDay));

		return petRequest;
	}
}
