package com.petstore.service.impl;

import com.petstore.entity.Pet;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.PetAlredyExistsException;
import com.petstore.repository.PetRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceImplClass {

	@InjectMocks
	private PetServiceImpl petService;
	@Mock
	private ModelMapper modelMapper;

	@Mock
	private PetRepository petRepository;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreatePetSuccess() {
		PetRequest petRequest = new PetRequest();
		petRequest.setOwner(1L);
		petRequest.setName("Toffi");
		petRequest.setPetType(PetType.CAT);
		petRequest.setDescription("The best Cat");
		petRequest.setDateOfBirth(LocalDate.of(2014, 02, 02));
		petRequest.setPrice(new BigDecimal(200.12));
		petRequest.setRating(5);

		Pet petEntity = new Pet();
		petEntity.setOwner(1L);
		petEntity.setName("Toffi");
		petEntity.setPetType(PetType.CAT);
		petEntity.setDescription("The best Cat");
		petEntity.setDateOfBirth(LocalDate.of(2014,02,02));
		petEntity.setPrice(new BigDecimal(200.12));
		petEntity.setRating(5);

		when(modelMapper.map(petRequest, Pet.class)).thenReturn(petEntity);
		when(petRepository.save(petEntity)).thenReturn(petEntity);

		Pet result = petService.createPet(petRequest);

		assertNotNull(result);
		assertEquals(1L, result.getOwner().longValue());
		assertEquals("Toffi", result.getName());
		assertEquals(PetType.CAT, result.getPetType());
		assertEquals("The best Cat", result.getDescription());
		assertEquals(LocalDate.of(2014,02,02), result.getDateOfBirth());
		assertEquals(new BigDecimal(9), result.getPrice());
		assertEquals(5, result.getRating());
	}

	@Test
	public void testCreatePetAlreadyExists() {
		PetRequest petRequest = new PetRequest();
		petRequest.setOwner(1L);
		petRequest.setName("Toffi");
		petRequest.setPetType(PetType.CAT);
		petRequest.setDescription("The best Cat");
		petRequest.setDateOfBirth(LocalDate.of(2014, 02, 02));
		petRequest.setPrice(new BigDecimal(200.12));
		petRequest.setRating(5);

		Pet petEntity = new Pet();
		petEntity.setOwner(1L);
		petEntity.setName("Toffi");
		petEntity.setPetType(PetType.CAT);
		petEntity.setDescription("The best Cat");
		petEntity.setDateOfBirth(LocalDate.of(2014,02,02));
		petEntity.setPrice(new BigDecimal(200.12));
		petEntity.setRating(5);

		when(modelMapper.map(petRequest, Pet.class)).thenReturn(petEntity);
		doThrow(DataIntegrityViolationException.class).when(petRepository).save(petEntity);

		assertThrows(PetAlredyExistsException.class, () -> petService.createPet(petRequest));
	}

	@Test
	public void testCreatePetNullInput() {
		assertThrows(IllegalArgumentException.class, () -> petService.createPet(null));
	}
}
