package com.petstore.service.impl;

import com.petstore.entity.User;
import com.petstore.exception.UserNotFoundException;
import com.petstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
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
		// Arrange
		Long userId = 1L;
		User expectedUser = new User(userId, "Bojan", "Vrshkovski", "bojan@gmail.com", new BigDecimal(200.2));
		when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

		// Act
		User result = userService.getUserById(userId);

		// Assert
		assertNotNull(result);
		assertEquals(userId, result.getUserId());
		assertEquals("Bojan", result.getFirstName());
		assertEquals("Vrshkovski", result.getLastName());
		assertEquals("bojan@gmail.com", result.getEmailAddress());
		assertEquals(new BigDecimal(200.2), result.getBudget());
	}

	@Test
	public void testGetUserByIdUserNotFound() {
		// Arrange
		Long userId = 999L; // An ID that doesn't exist in the repository
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
	}

	@Test
	public void testGetUserByIdNullUserId() {
		// Arrange
		Long userId = null; // Invalid input

		// Act & Assert
		assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
	}
}
