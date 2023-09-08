package com.petstore.controller;

import com.petstore.entity.Pet;
import com.petstore.entity.User;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.NoPetsFoundException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.service.PetService;
import com.petstore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PetControllerTest {
	@InjectMocks
	private PetController petController;
	@Mock
	private PetService petService;
	@Mock
	private UserService userService;

	@Test
	public void testCreatePetSuccess() {
		PetRequest petRequest = new PetRequest();
		petRequest.setOwner(1L);
		petRequest.setName("Toffi");
		petRequest.setPetType(PetType.CAT);
		petRequest.setDescription("The best Cat");
		petRequest.setDateOfBirth(LocalDate.of(2014,02,02));
		petRequest.setPrice(new BigDecimal(200.12));
		petRequest.setRating(5);

		Pet createdPet = new Pet();
		createdPet.setOwner(1L);
		createdPet.setName("Toffi");
		createdPet.setPetType(PetType.CAT);
		createdPet.setDescription("The best Cat");
		createdPet.setDateOfBirth(LocalDate.of(2014,02,02));
		createdPet.setPrice(new BigDecimal(200.12));
		createdPet.setRating(5);

		when(petService.createPet(petRequest)).thenReturn(createdPet);

		Pet result = petController.createPet(petRequest);

		assertNotNull(result);
		assertEquals(1L, result.getOwner().longValue());
		assertEquals("Toffi", result.getName());
		assertEquals(PetType.CAT, result.getPetType());
		assertEquals("The best Cat", result.getDescription());
		assertEquals(LocalDate.of(2014,02,02), result.getDateOfBirth());
		assertEquals(new BigDecimal(200.12), result.getPrice());
		assertEquals(5, result.getRating());
	}

	@Test
	public void testCreatePetFailure() {
		PetRequest petRequest = new PetRequest();
		petRequest.setOwner(1L);
		petRequest.setName("Toffi");
		petRequest.setPetType(PetType.CAT);
		petRequest.setDescription("The best Cat");
		petRequest.setDateOfBirth(LocalDate.of(2014,02,02));
		petRequest.setPrice(new BigDecimal(200.12));
		petRequest.setRating(5);

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
			new PetDto(2L,1L,"Toffi",PetType.CAT,"The best cat",LocalDate.of(2018,02,02),new BigDecimal(123.22),5),
			new PetDto(3L,2L,"Buffi",PetType.DOG,"The best cat",LocalDate.of(2019,04,10),new BigDecimal(100.20),3)
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
		Long userId = 1L;
		Long petId = 2L;
		User testUser = new User();
		testUser.setBudget(new BigDecimal(1000));

		UserDto testUserDto = new UserDto();
		testUserDto.setBudget(new BigDecimal(1000));

		Pet testPet = new Pet();
		testPet.setPrice(new BigDecimal(500));
		testPet.setOwner(null);

		PetDto testPetDto = new PetDto();
		testPetDto.setPrice(new BigDecimal(500));
		testPetDto.setOwner(null);

		when(petService.buy(userId, petId)).thenReturn(testPet);
		when(petService.readPetById(petId)).thenReturn(testPetDto);
		when(userService.readUserById(userId)).thenReturn(testUserDto);

		Pet result = petController.buy(userId, petId);

		assertNotNull(result);

		verify(petService, times(1)).buy(userId, petId);
	}

	public void testReadPetByIdSuccess() {
		Long petId = 1L;
		PetDto expectedPet = new PetDto(petId, 1L,"Toffi",PetType.CAT,"The best cat",LocalDate.of(2018,02,02),new BigDecimal(123.22),5);

		when(petService.readPetById(petId)).thenReturn(expectedPet);

		PetDto result = petController.readPetById(petId);

		assertNotNull(result);
		assertEquals(petId, result.getPetId());
		assertEquals(1L, result.getOwner());
		assertEquals("Toffi", result.getName());
		assertEquals(PetType.CAT, result.getPetType());
		assertEquals(LocalDate.of(2018,02,02), result.getDateOfBirth());
		assertEquals(new BigDecimal(123.22), result.getPrice());
		assertEquals(5, result.getRating());
	}

	@Test
	public void testReadPetByIdNotFound() {
		Long petId = 2L;

		when(petService.readPetById(petId)).thenThrow(PetNotFoundException.class);

		assertThrows(PetNotFoundException.class, () -> petController.readPetById(petId));
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


	private List<Pet> createDummyPets(int count) {
		List<Pet> pets = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			Pet pet = new Pet();
			pet.setPetId((long) i);
			pet.setOwner((long) i);
			pet.setName("Toffi");
			pet.setPetType(PetType.CAT);
			pet.setDescription("The best cat");
			pet.setPrice(new BigDecimal(100.00));
			pet.setRating(10);

			pets.add(pet);
		}
		return pets;
	}

}
