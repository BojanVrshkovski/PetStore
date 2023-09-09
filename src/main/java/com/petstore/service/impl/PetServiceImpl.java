package com.petstore.service.impl;

import com.github.javafaker.Faker;
import com.petstore.entity.BuyLogEntry;
import com.petstore.entity.Pet;
import com.petstore.entity.User;
import com.petstore.entity.dto.BuyLogEntryDto;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.dto.PurchaseSummary;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.NoBuyLogEntriesException;
import com.petstore.exception.NoPetsFoundException;
import com.petstore.exception.NotEnoughBudgetException;
import com.petstore.exception.PetAlredyExistsException;
import com.petstore.exception.PetAlredyHasOwnerException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.repository.BuyLogEntryRepository;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.petstore.constants.LoggerAndExceptionConstants.CAT_SUCCESSFULLY_BOUGHT;
import static com.petstore.constants.LoggerAndExceptionConstants.CONUT_RANGE_1_TO_20;
import static com.petstore.constants.LoggerAndExceptionConstants.DOG_SUCCESSFULLY_BOUGHT;
import static com.petstore.constants.LoggerAndExceptionConstants.NO_BUY_LOG_ENTRIES_FOUND;
import static com.petstore.constants.LoggerAndExceptionConstants.NO_PETS_FOUND;
import static com.petstore.constants.LoggerAndExceptionConstants.PET_ALREDY_EXISTS;
import static com.petstore.constants.LoggerAndExceptionConstants.PET_ALREDY_HAS_OWNER;
import static com.petstore.constants.LoggerAndExceptionConstants.PET_IS_BEING_FETCHED;
import static com.petstore.constants.LoggerAndExceptionConstants.PET_IS_SUCCESSFULLY_ADDED_DB;
import static com.petstore.constants.LoggerAndExceptionConstants.PET_NOT_FOUND;
import static com.petstore.constants.LoggerAndExceptionConstants.PET_REQUEST_IS_NULL;
import static com.petstore.constants.LoggerAndExceptionConstants.PURCHASE_SUCCESSFUL;
import static com.petstore.constants.LoggerAndExceptionConstants.RETRIVING_ALL_BUY_LOG_ENTRIES;
import static com.petstore.constants.LoggerAndExceptionConstants.RETRIVING_ALL_PETS;
import static com.petstore.constants.LoggerAndExceptionConstants.USER_COULD_NOT_MAKE_PURCHASE;
import static com.petstore.constants.LoggerAndExceptionConstants.USER_NOT_ENOUGH_BUDGET;
import static com.petstore.constants.LoggerAndExceptionConstants.USER_NOT_FOUND;

@Service
public class PetServiceImpl implements PetService {
	private static final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);
	private final PetRepository petRepository;
	private final UserRepository userRepository;
	private final BuyLogEntryRepository buyLogEntryRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public PetServiceImpl(PetRepository petRepository, UserRepository userRepository,
	                      BuyLogEntryRepository buyLogEntryRepository, ModelMapper modelMapper) {
		this.petRepository = petRepository;
		this.userRepository = userRepository;
		this.buyLogEntryRepository = buyLogEntryRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Pet createPet(PetRequest petRequest) {
		if (petRequest == null) {
			throw new IllegalArgumentException(PET_REQUEST_IS_NULL);
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
			log.info(String.format(PET_IS_SUCCESSFULLY_ADDED_DB,petRequest.getName()));
		}catch (DataIntegrityViolationException e){
			log.error(String.format(PET_ALREDY_EXISTS));
			throw new PetAlredyExistsException(PET_ALREDY_EXISTS);
		}
		return pet;
	}

	@Override
	public List<PetDto> readAllPets() {
		List<Pet> pets = petRepository.findAll();
		if (pets.isEmpty()) {
			log.error(String.format(NO_PETS_FOUND));
			throw new NoPetsFoundException(NO_PETS_FOUND);
		}
		log.info(String.format(RETRIVING_ALL_PETS));
		return pets.stream()
		            .map(pet -> modelMapper.map(pet, PetDto.class))
		            .collect(Collectors.toList());
	}

	@Override
	public Pet buy(Long userId, Long petId) {
		Optional<User> userOptional = userRepository.findById(userId);
		Optional<Pet> petOptional = petRepository.findById(petId);
		BuyLogEntry logEntry = new BuyLogEntry();

		if (userOptional.isEmpty()) {
			log.error(String.format(USER_NOT_FOUND));
			throw new UserNotFoundException();
		}else if (petOptional.isEmpty()){
			log.error(String.format(PET_NOT_FOUND));
			throw new PetNotFoundException();
		}

		User user = userOptional.get();
		Pet pet = petOptional.get();

		if (pet.getOwner() != null) {
			logEntry.setExecutionDate(LocalDateTime.now());
			logEntry.setAllowedToBuy(false);
			logEntry.setUserId(user.getUserId());
			buyLogEntryRepository.save(logEntry);
			log.error(String.format(PET_ALREDY_HAS_OWNER));
			throw new PetAlredyHasOwnerException(PET_ALREDY_HAS_OWNER);
		}

		BigDecimal userBudget = user.getBudget();
		BigDecimal petPrice = pet.getPrice();

		if (userBudget.compareTo(petPrice) < 0) {
			logEntry.setExecutionDate(LocalDateTime.now());
			logEntry.setAllowedToBuy(false);
			logEntry.setUserId(user.getUserId());
			buyLogEntryRepository.save(logEntry);
			log.error(String.format(USER_NOT_ENOUGH_BUDGET,user.getFirstName()));
			throw new NotEnoughBudgetException(USER_NOT_ENOUGH_BUDGET);
		}


		user.setBudget(userBudget.subtract(petPrice));

		pet.setOwner(user.getUserId());

		userRepository.save(user);
		petRepository.save(pet);


		logEntry.setExecutionDate(LocalDateTime.now());
		logEntry.setAllowedToBuy(true);
		logEntry.setUserId(user.getUserId());

		buyLogEntryRepository.save(logEntry);

		if (pet.getPetType() == PetType.CAT) {
			log.info(String.format(CAT_SUCCESSFULLY_BOUGHT));
			System.out.println("Meow, cat " + pet.getName() + " has owner " + user.getFirstName());
		} else if (pet.getPetType() == PetType.DOG) {
			log.info(String.format(DOG_SUCCESSFULLY_BOUGHT));
			System.out.println("Woof, dog " + pet.getName() + " has owner " + user.getFirstName());
		}

		log.info(String.format(PURCHASE_SUCCESSFUL));
		return pet;
	}

	@Override
	public PurchaseSummary buyAll() {
		List<User> users = userRepository.findAll();
		List<Pet> availablePets = petRepository.findAvailablePets();

		int successfulPurchases = 0;
		int failedPurchases = 0;

		for (User user : users) {
			boolean hasPet = false;

			for (Pet pet : availablePets) {
				if (!hasPet) {
					try {
						buy(user.getUserId(), pet.getPetId());
						successfulPurchases++;
						hasPet = true;
					} catch (Exception e) {
						log.error(String.format(USER_COULD_NOT_MAKE_PURCHASE, user.getFirstName(), e.getMessage()));
						failedPurchases++;
					}
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
		log.info(String.format(PET_IS_BEING_FETCHED, petId));
		Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
		return modelMapper.map(pet, PetDto.class);
	}

	@Override
	public List<Pet> createRandomPets(int count) {
		if (count <= 0 || count > 20) {
			throw new IllegalArgumentException(CONUT_RANGE_1_TO_20);
		}

		List<Pet> createdPets = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			PetRequest randomPetRequest = generateRandomPetData();
			Pet createdPet = createPet(randomPetRequest);
			createdPets.add(createdPet);
		}

		return createdPets;
	}

	@Override
	public List<BuyLogEntryDto> readBuyHistory() {
		List<BuyLogEntry> buyLogEntries = buyLogEntryRepository.findAll();
		if (buyLogEntries.isEmpty()) {
			log.error(String.format(NO_BUY_LOG_ENTRIES_FOUND));
			throw new NoBuyLogEntriesException(NO_BUY_LOG_ENTRIES_FOUND);
		}
		log.info(String.format(RETRIVING_ALL_BUY_LOG_ENTRIES));
		return buyLogEntries.stream()
		            .map(buyLogEntrie -> modelMapper.map(buyLogEntrie, BuyLogEntryDto.class))
		            .collect(Collectors.toList());
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
