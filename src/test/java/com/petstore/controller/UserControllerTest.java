package com.petstore.controller;

import com.petstore.entity.dto.UserDto;
import com.petstore.exception.NoUsersFoundException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
		UserDto expectedUser = new UserDto(userId, "Bojan", "Vrshkovski", "bojan@gmail.com", new BigDecimal(200.2));

		when(userService.readUserById(userId)).thenReturn(expectedUser);

		UserDto result = userController.readUserById(userId);

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

		when(userService.readUserById(userId)).thenThrow(UserNotFoundException.class);

		assertThrows(UserNotFoundException.class, () -> userController.readUserById(userId));
	}

	@Test
	public void testReadAllUsersSuccess() {

		List<UserDto> expectedUsers = Arrays.asList(
			new UserDto(2L,"Bojan","Vrshkovski","b@gmail.com",new BigDecimal(123.22)),
			new UserDto(3L,"Petar","Vrshkovski","petar@gmail.com",new BigDecimal(12322.22))
		);

		when(userService.readAllUsers()).thenReturn(expectedUsers);

		List<UserDto> result = userController.readAllUsers();

		assertNotNull(result);
		assertEquals(expectedUsers.size(), result.size());
	}

	@Test
	public void testReadAllUsersFailure() {
		when(userService.readAllUsers()).thenThrow(NoUsersFoundException.class);

		assertThrows(NoUsersFoundException.class, () -> userController.readAllUsers());
	}

}
