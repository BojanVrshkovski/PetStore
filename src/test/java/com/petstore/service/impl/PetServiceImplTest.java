package com.petstore.service.impl;

import com.petstore.entity.Pet;
import com.petstore.entity.dto.PetDto;
import com.petstore.entity.enums.PetType;
import com.petstore.entity.request.PetRequest;
import com.petstore.exception.NoPetsFoundException;
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
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceImplTest {

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

	@Test
	public void testReadAllUsersSuccess() {
		Pet pet = new Pet(2L,1L,"Toffi",PetType.CAT,"The best cat",LocalDate.of(2018,02,02),new BigDecimal(123.22),5);

		Pet pet1 = new Pet(3L,2L,"Buffi",PetType.DOG,"The best cat",LocalDate.of(2019,04,10),new BigDecimal(100.20),3);

		List<Pet> mockPets = List.of(pet, pet1);
		when(petRepository.findAll()).thenReturn(mockPets);

		PetDto petDto = new PetDto(2L,1L,"Toffi",PetType.CAT,"The best cat",LocalDate.of(2018,02,02),new BigDecimal(123.22),5);

		PetDto petDto1 = new PetDto(3L,2L,"Buffi",PetType.DOG,"The best cat",LocalDate.of(2019,04,10),new BigDecimal(100.20),3);

		List<PetDto> expectedPetDtos = List.of(petDto, petDto1);
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
}