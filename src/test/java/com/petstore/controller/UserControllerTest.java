package com.petstore.controller;

import com.petstore.entity.User;
import com.petstore.exception.UserNotFoundException;
import com.petstore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	@Test
	public void testGetUserByIdSuccess() {
		Long userId = 1L;
		User expectedUser = new User(userId,"Bojan","Vrshkovski","bojan@gmail.com",new BigDecimal(200.2));

		when(userService.getUserById(userId)).thenReturn(expectedUser);

		User result = userController.userById(userId);

		assertNotNull(result);
		assertEquals(userId, result.getUserId());
		assertEquals("Bojan", result.getFirstName());
		assertEquals("Vrshkovski", result.getLastName());
		assertEquals("bojan@gmail.com", result.getEmailAddress());
		assertEquals(new BigDecimal(200.2), result.getBudget());
	}

	@Test
	public void testGetUserByIdNotFound() {
		Long userId = 2L;

		when(userService.getUserById(userId)).thenThrow(UserNotFoundException.class);

		assertThrows(UserNotFoundException.class, () -> userController.userById(userId));
	}
}
