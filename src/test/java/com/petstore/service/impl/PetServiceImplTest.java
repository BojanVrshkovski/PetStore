package com.petstore.service.impl;

import com.petstore.entity.BuyLogEntry;
import com.petstore.entity.Pet;
import com.petstore.entity.User;
import com.petstore.entity.dto.BuyLogEntryDto;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.NoBuyLogEntriesException;
import com.petstore.exception.NoPetsFoundException;
import com.petstore.exception.NotEnoughBudgetException;
import com.petstore.exception.PetAlredyExistsException;
import com.petstore.exception.PetAlredyHasOwnerException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.repository.BuyLogEntryRepository;
import com.petstore.repository.PetRepository;
import com.petstore.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import static com.petstore.util.BuyLogEntryFactory.getDefaultBuyLogEntry;
import static com.petstore.util.BuyLogEntryFactory.getDefaultBuyLogEntryDto;
import static com.petstore.util.PetFactory.getDefaultPet;
import static com.petstore.util.PetFactory.getDefaultPetDto;
import static com.petstore.util.PetFactory.getDefaultPetRequest;
import static com.petstore.util.UserFactory.getDefaultUser;
import static com.petstore.util.UserFactory.getDefaultUserDto;
import static org.mockito.Mockito.verify;
import org.springframework.dao.DataIntegrityViolationException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static com.petstore.util.PetConstants.PET_ID;
import static com.petstore.util.PetConstants.OWNER;
import static com.petstore.util.PetConstants.NAME;
import static com.petstore.util.PetConstants.PET_TYPE;
import static com.petstore.util.PetConstants.DESCRIPTION;
import static com.petstore.util.PetConstants.DATE_OF_BIRTH;
import static com.petstore.util.PetConstants.PRICE;
import static com.petstore.util.PetConstants.RAITING;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceImplTest {

	private PetRequest petRequest;
	private Pet pet;
	private PetDto petDto;
	private UserDto userDto;
	private BuyLogEntryDto buyLogEntryDto;
	private BuyLogEntry buyLogEntry;
	private User user;
	@InjectMocks
	private PetServiceImpl petService;
	@Mock
	private ModelMapper modelMapper;

	@Mock
	private PetRepository petRepository;
	@Mock
	private BuyLogEntryRepository buyLogEntryRepository;
	@Mock
	private UserRepository userRepository;

	@Before
	public void setup() {
		petRequest = getDefaultPetRequest();
		pet = getDefaultPet();
		petDto = getDefaultPetDto();
		userDto = getDefaultUserDto();
		user = getDefaultUser();
		buyLogEntryDto = getDefaultBuyLogEntryDto();
		buyLogEntry = getDefaultBuyLogEntry();
	}

	@Test
	public void testCreatePetSuccess() {
		when(modelMapper.map(petRequest, Pet.class)).thenReturn(pet);
		when(petRepository.save(pet)).thenReturn(pet);

		Pet result = petService.createPet(petRequest);

		assertNotNull(result);
		assertEquals(OWNER, result.getOwner().longValue());
		assertEquals(NAME, result.getName());
		assertEquals(PET_TYPE, result.getPetType());
		assertEquals(DESCRIPTION, result.getDescription());
		assertEquals(DATE_OF_BIRTH, result.getDateOfBirth());
		assertEquals(PRICE, new BigDecimal(200.00));
		assertEquals(RAITING, result.getRating());
	}

	@Test
	public void testCreatePetAlreadyExists() {
		when(modelMapper.map(petRequest, Pet.class)).thenReturn(pet);
		doThrow(DataIntegrityViolationException.class).when(petRepository).save(pet);

		assertThrows(PetAlredyExistsException.class, () -> petService.createPet(petRequest));
	}

	@Test
	public void testCreatePetNullInput() {
		assertThrows(IllegalArgumentException.class, () -> petService.createPet(null));
	}

	@Test
	public void testReadAllUsersSuccess() {
		List<Pet> mockPets = List.of(pet, pet);
		when(petRepository.findAll()).thenReturn(mockPets);

		List<PetDto> expectedPetDtos = List.of(petDto, petDto);
		when(modelMapper.map(any(Pet.class), eq(PetDto.class)))
			.thenReturn(expectedPetDtos.get(0), expectedPetDtos.get(1));

		List<PetDto> actualPetDtos = petService.readAllPets();

		assertEquals(expectedPetDtos.size(), actualPetDtos.size());
		assertEquals(expectedPetDtos, actualPetDtos);
	}

	@Test
	public void testReadAllUsersEmptyList() {

		when(petRepository.findAll()).thenReturn(Collections.emptyList());

		assertThrows(NoPetsFoundException.class, () -> petService.readAllPets());
	}

	@Test
	public void testBuyPetSuccess() {
		pet.setOwner(null);

		when(userRepository.findById(OWNER)).thenReturn(Optional.of(user));
		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

		Pet result = petService.buy(OWNER, PET_ID);
		pet.setOwner(OWNER);

		assertNotNull(result);
		assertEquals(OWNER, pet.getOwner());

		verify(userRepository, times(1)).save(user);
		verify(petRepository, times(1)).save(pet);
	}

	@Test
	public void testBuyPetPetNotFound() {
		when(userRepository.findById(OWNER)).thenReturn(Optional.of(user));
		when(petRepository.findById(PET_ID)).thenReturn(Optional.empty());

		assertThrows(PetNotFoundException.class, () -> petService.buy(OWNER, PET_ID));

		verify(userRepository, times(1)).findById(OWNER);
	}

	@Test
	public void testBuyPetAlreadyHasOwner() {
		when(userRepository.findById(OWNER)).thenReturn(Optional.of(user));
		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

		assertThrows(PetAlredyHasOwnerException.class, () -> petService.buy(OWNER, PET_ID));

		verify(userRepository, times(1)).findById(OWNER);
	}

	@Test
	public void testBuyPetNotEnoughBudget() {
		user.setBudget(new BigDecimal(10.00));
		pet.setOwner(null);

		when(userRepository.findById(OWNER)).thenReturn(Optional.of(user));
		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

		assertThrows(NotEnoughBudgetException.class, () -> petService.buy(OWNER, PET_ID));

		verify(petRepository, times(1)).findById(PET_ID);
	}

	@Test
	public void testReadPetByIdSuccess() {
		Pet expectedPet = pet;

		when(modelMapper.map(expectedPet, PetDto.class)).thenReturn(petDto);

		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(expectedPet));

		PetDto result = petService.readPetById(PET_ID);

		assertNotNull(result);
		assertEquals(PET_ID, result.getPetId());
		assertEquals(OWNER, result.getOwner());
		assertEquals(NAME, result.getName());
		assertEquals(PET_TYPE, result.getPetType());
		assertEquals(DATE_OF_BIRTH, result.getDateOfBirth());
		assertEquals(PRICE, result.getPrice());
		assertEquals(RAITING, result.getRating());
	}

	@Test
	public void testGetUserByIdUserNotFound() {
		when(petRepository.findById(PET_ID)).thenReturn(Optional.empty());

		assertThrows(PetNotFoundException.class, () -> petService.readPetById(PET_ID));
	}

	@Test
	public void testGetUserByIdNullUserId() {
		Long petId = null;

		assertThrows(PetNotFoundException.class, () -> petService.readPetById(petId));
	}

	@Test
	public void testCreateUserNullInput() {
		assertThrows(IllegalArgumentException.class, () -> petService.createPet(null));
	}

	@Test
	public void testCreateRandomUsersInvalidCount() {
		int count = 0;

		assertThrows(IllegalArgumentException.class, () -> petService.createRandomPets(count));
	}

	@Test
	public void testCreateRandomUsersExceedsLimit() {
		int count = 21;

		assertThrows(IllegalArgumentException.class, () -> petService.createRandomPets(count));
	}

	@Test
	public void testReadAllBuyLogsSuccess() {
		List<BuyLogEntry> mockBuyLogs = List.of(buyLogEntry, buyLogEntry);
		when(buyLogEntryRepository.findAll()).thenReturn(mockBuyLogs);

		List<BuyLogEntryDto> expectedBuyLogEntryDtos = List.of(buyLogEntryDto, buyLogEntryDto);
		when(modelMapper.map(any(BuyLogEntry.class), eq(BuyLogEntryDto.class)))
			.thenReturn(expectedBuyLogEntryDtos.get(0), expectedBuyLogEntryDtos.get(1));

		List<BuyLogEntryDto> actualBuyLogEntryDtos = petService.readBuyHistory();

		assertEquals(expectedBuyLogEntryDtos.size(), actualBuyLogEntryDtos.size());
		assertEquals(expectedBuyLogEntryDtos, actualBuyLogEntryDtos);
	}

	@Test
	public void testReadAllBuyLogsEmptyList(){
		when(buyLogEntryRepository.findAll()).thenReturn(Collections.emptyList());

		assertThrows(NoBuyLogEntriesException.class, () -> petService.readBuyHistory());
	}

}
