package com.petstore.service.impl;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.exception.UserNotFoundException;
import com.petstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetUserByIdSuccess() {
		Long userId = 1L;
		User expectedUser = new User(userId, "Bojan", "Vrshkovski", "bojan@gmail.com", new BigDecimal(200.2));

		ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
		when(modelMapper.map(expectedUser, UserDto.class)).thenReturn(new UserDto(userId, "Bojan", "Vrshkovski", "bojan@gmail.com", new BigDecimal(200.2)));

		UserServiceImpl userService = new UserServiceImpl(userRepository, modelMapper);
		when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

		UserDto result = userService.readUserById(userId);

		assertNotNull(result);
		assertEquals(userId, result.getUserId());
		assertEquals("Bojan", result.getFirstName());
		assertEquals("Vrshkovski", result.getLastName());
		assertEquals("bojan@gmail.com", result.getEmailAddress());
		assertEquals(new BigDecimal(200.2), result.getBudget());
	}

	@Test
	public void testGetUserByIdUserNotFound() {

		Long userId = 999L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.readUserById(userId));
	}

	@Test
	public void testGetUserByIdNullUserId() {
		Long userId = null;

		assertThrows(UserNotFoundException.class, () -> userService.readUserById(userId));
	}
}
