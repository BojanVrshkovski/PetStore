package com.petstore.controller;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;
import com.petstore.exception.NoUsersFoundException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static com.petstore.util.UserFactory.getDefaultUserRequest;
import static com.petstore.util.UserFactory.getDefaultUser;
import static com.petstore.util.UserFactory.createDummyUsers;
import static com.petstore.util.UserFactory.getDefaultUserDto;
import static com.petstore.util.UserConstants.USER_ID;
import static com.petstore.util.UserConstants.EMAIL_ADDRESS;
import static com.petstore.util.UserConstants.LAST_NAME;
import static com.petstore.util.UserConstants.FIRST_NAME;
import static com.petstore.util.UserConstants.BUDGET;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	private User user;
	private UserDto userDto;
	private UserRequest userRequest;
	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	@Before
	public void setup(){
		user = getDefaultUser();
		userDto = getDefaultUserDto();
		userRequest = getDefaultUserRequest();
	}
	@Test
	public void testGetUserByIdSuccess() {
		UserDto expectedUser = userDto;

		when(userService.readUserById(USER_ID)).thenReturn(expectedUser);

		UserDto result = userController.readUserById(USER_ID);

		assertNotNull(result);
		assertEquals(USER_ID, result.getUserId());
		assertEquals(FIRST_NAME, result.getFirstName());
		assertEquals(LAST_NAME, result.getLastName());
		assertEquals(EMAIL_ADDRESS, result.getEmailAddress());
		assertEquals(BUDGET, result.getBudget());
	}

	@Test
	public void testGetUserByIdNotFound() {
		when(userService.readUserById(USER_ID)).thenThrow(UserNotFoundException.class);

		assertThrows(UserNotFoundException.class, () -> userController.readUserById(USER_ID));
	}

	@Test
	public void testReadAllUsersSuccess() {
		List<UserDto> expectedUsers = Arrays.asList(
			userDto,
			userDto
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

	@Test
	public void testCreateUserSuccess() {
		when(userService.createUser(userRequest)).thenReturn(user);

		User result = userController.createUser(userRequest);

		assertNotNull(result);
		assertEquals(USER_ID, result.getUserId().longValue());
		assertEquals(FIRST_NAME, result.getFirstName());
		assertEquals(LAST_NAME, result.getLastName());
		assertEquals(EMAIL_ADDRESS, result.getEmailAddress());
		assertEquals(BUDGET, result.getBudget());
	}

	@Test
	public void testCreateUserFailure() {
		when(userService.createUser(userRequest)).thenThrow(new DataIntegrityViolationException("User with that email already exists"));

		try {
			userController.createUser(userRequest);
			fail("Expected exception was not thrown");
		} catch (Exception ex) {
			assertEquals("User with that email already exists", ex.getMessage());
		}

		verify(userService, times(1)).createUser(userRequest);
	}

	@Test
	public void testCreateRandomUsersSuccess() {
		int count = 5;
		List<User> expectedUsers = createDummyUsers(count);

		when(userService.createRandomUsers(count)).thenReturn(expectedUsers);

		List<User> result = userController.createRandomUsers(count);

		assertNotNull(result);
		assertEquals(count, result.size());

		for (int i = 0; i < count; i++) {
			User expectedUser = expectedUsers.get(i);
			User actualUser = result.get(i);

			assertNotNull(actualUser.getUserId());
			assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
			assertEquals(expectedUser.getLastName(), actualUser.getLastName());
			assertEquals(expectedUser.getEmailAddress(), actualUser.getEmailAddress());
			assertEquals(expectedUser.getBudget(), actualUser.getBudget());
		}
	}
}
