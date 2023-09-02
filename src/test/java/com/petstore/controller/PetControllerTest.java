package com.petstore.controller;

import com.petstore.entity.Pet;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;
import com.petstore.service.PetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
