package com.petstore.controller;

import com.petstore.entity.Pet;
import com.petstore.entity.dto.BuyLogEntryDto;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.NoBuyLogEntriesException;
import com.petstore.exception.NoPetsFoundException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.service.PetService;
import com.petstore.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.petstore.util.PetFactory.getDefaultPet;
import static com.petstore.util.PetFactory.getDefaultPetDto;
import static com.petstore.util.PetFactory.getDefaultPetRequest;
import static com.petstore.util.UserFactory.getDefaultUserDto;
import static com.petstore.util.PetFactory.createDummyPets;
import static com.petstore.util.BuyLogEntryFactory.getDefaultBuyLogEntryDto;
import static com.petstore.util.PetConstants.PET_ID;
import static com.petstore.util.PetConstants.OWNER;
import static com.petstore.util.PetConstants.NAME;
import static com.petstore.util.PetConstants.PET_TYPE;
import static com.petstore.util.PetConstants.DESCRIPTION;
import static com.petstore.util.PetConstants.DATE_OF_BIRTH;
import static com.petstore.util.PetConstants.PRICE;
import static com.petstore.util.PetConstants.RAITING;


@RunWith(MockitoJUnitRunner.class)
public class PetControllerTest {
	private PetRequest petRequest;
	private Pet pet;
	private PetDto petDto;
	private UserDto userDto;
	private BuyLogEntryDto buyLogEntryDto;
	@InjectMocks
	private PetController petController;
	@Mock
	private PetService petService;
	@Mock
	private UserService userService;

	@Before
	public void setup(){
		petRequest = getDefaultPetRequest();
		pet = getDefaultPet();
		petDto = getDefaultPetDto();
		userDto = getDefaultUserDto();
		buyLogEntryDto = getDefaultBuyLogEntryDto();
	}
	@Test
	public void testCreatePetSuccess() {
		Pet createdPet = pet;

		when(petService.createPet(petRequest)).thenReturn(createdPet);

		Pet result = petController.createPet(petRequest);

		assertNotNull(result);
		assertEquals(OWNER, result.getOwner().longValue());
		assertEquals(NAME, result.getName());
		assertEquals(PET_TYPE, result.getPetType());
		assertEquals(DESCRIPTION, result.getDescription());
		assertEquals(DATE_OF_BIRTH, result.getDateOfBirth());
		assertEquals(PRICE, result.getPrice());
		assertEquals(RAITING, result.getRating());
	}

	@Test
	public void testCreatePetFailure() {
		when(petService.createPet(petRequest)).thenThrow(new DataIntegrityViolationException("Pet alredy exists"));

		try {
			petController.createPet(petRequest);
			fail("Expected exception was not thrown");
		} catch (Exception ex) {
			assertEquals("Pet alredy exists", ex.getMessage());
		}

		verify(petService, times(1)).createPet(petRequest);
	}

	@Test
	public void testReadAllPetsSuccess() {
		List<PetDto> expectedPets = Arrays.asList(
			petDto,
      petDto
		);

		when(petService.readAllPets()).thenReturn(expectedPets);

		List<PetDto> result = petController.readAllPets();

		assertNotNull(result);
		assertEquals(expectedPets.size(), result.size());
	}

	@Test
	public void testReadAllPetsFailure() {
		when(petService.readAllPets()).thenThrow(NoPetsFoundException.class);

		assertThrows(NoPetsFoundException.class, () -> petController.readAllPets());
	}

	@Test
	public void testBuyPetSuccess() {
		when(petService.buy(OWNER, PET_ID)).thenReturn(pet);

		Pet result = petController.buy(OWNER, PET_ID);

		assertNotNull(result);

		verify(petService, times(1)).buy(OWNER, PET_ID);
	}

	@Test
	public void testReadPetByIdSuccess() {
		PetDto expectedPet = petDto;

		when(petService.readPetById(PET_ID)).thenReturn(expectedPet);

		PetDto result = petController.readPetById(PET_ID);

		assertNotNull(result);
		assertEquals(PET_ID, result.getPetId());
		assertEquals(OWNER, result.getOwner());
		assertEquals(NAME , result.getName());
		assertEquals(PET_TYPE, result.getPetType());
		assertEquals(DATE_OF_BIRTH, result.getDateOfBirth());
		assertEquals(PRICE, result.getPrice());
		assertEquals(RAITING, result.getRating());
	}

	@Test
	public void testReadPetByIdNotFound() {
		when(petService.readPetById(PET_ID)).thenThrow(PetNotFoundException.class);

		assertThrows(PetNotFoundException.class, () -> petController.readPetById(PET_ID));
	}

	@Test
	public void testCreateRandomUsersSuccess() {
		int count = 5;
		List<Pet> expectedPets = createDummyPets(count);

		when(petService.createRandomPets(count)).thenReturn(expectedPets);

		List<Pet> result = petController.createRandomPets(count);

		assertNotNull(result);
		assertEquals(count, result.size());

		for (int i = 0; i < count; i++) {
			Pet expectedPet = expectedPets.get(i);
			Pet actualPet = result.get(i);

			assertNotNull(actualPet.getPetId());
			assertEquals(expectedPet.getOwner(), actualPet.getOwner());
			assertEquals(expectedPet.getName(), actualPet.getName());
			assertEquals(expectedPet.getPetType(), actualPet.getPetType());
			assertEquals(expectedPet.getDescription(), actualPet.getDescription());
			assertEquals(expectedPet.getDateOfBirth(), actualPet.getDateOfBirth());
			assertEquals(expectedPet.getRating(), actualPet.getRating());
			assertEquals(expectedPet.getPrice(), actualPet.getPrice());
		}
	}

	@Test
	public void testReadAllBuyLogsSuccess(){
		List<BuyLogEntryDto> expectedBuyLogs = Arrays.asList(
			buyLogEntryDto,
			buyLogEntryDto
		);

		when(petService.readBuyHistory()).thenReturn(expectedBuyLogs);

		List<BuyLogEntryDto> result = petController.readBuyHistory();

		assertNotNull(result);
		assertEquals(expectedBuyLogs.size(),result.size());
	}

	@Test
	public void testReadAllBuyLogsFailure(){
		when(petService.readBuyHistory()).thenThrow(NoBuyLogEntriesException.class);

		assertThrows(NoBuyLogEntriesException.class, () -> petController.readBuyHistory());
	}

}
