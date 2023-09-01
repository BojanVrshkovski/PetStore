package com.petstore.controller;

import com.petstore.entity.dto.UserDto;
import com.petstore.exception.NoUsersFoundException;
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
