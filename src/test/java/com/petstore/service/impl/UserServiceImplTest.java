package com.petstore.service.impl;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;
import com.petstore.exception.UserAlreadyExistException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.exception.NoUsersFoundException;
import com.petstore.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import static org.mockito.Mockito.doThrow;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static com.petstore.util.UserFactory.getDefaultUserRequest;
import static com.petstore.util.UserFactory.getDefaultUser;
import static com.petstore.util.UserFactory.getDefaultUserDto;
import static com.petstore.util.UserConstants.USER_ID;
import static com.petstore.util.UserConstants.EMAIL_ADDRESS;
import static com.petstore.util.UserConstants.LAST_NAME;
import static com.petstore.util.UserConstants.FIRST_NAME;
import static com.petstore.util.UserConstants.BUDGET;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	private User user;
	private UserRequest userRequest;
	private UserDto userDto;

	@InjectMocks
	private UserServiceImpl userService;
	@Mock
	private ModelMapper modelMapper;

	@Mock
	private UserRepository userRepository;

	@Before
	public void setup() {
		user = getDefaultUser();
		userDto = getDefaultUserDto();
		userRequest = getDefaultUserRequest();
	}

	@Test
	public void testGetUserByIdSuccess() {
		User expectedUser = user;

		ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
		when(modelMapper.map(expectedUser, UserDto.class)).thenReturn(userDto);

		UserServiceImpl userService = new UserServiceImpl(userRepository, modelMapper);
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(expectedUser));

		UserDto result = userService.readUserById(USER_ID);

		assertNotNull(result);
		assertEquals(USER_ID, result.getUserId());
		assertEquals(FIRST_NAME, result.getFirstName());
		assertEquals(LAST_NAME, result.getLastName());
		assertEquals(EMAIL_ADDRESS, result.getEmailAddress());
		assertEquals(BUDGET, result.getBudget());
	}

	@Test
	public void testGetUserByIdUserNotFound() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.readUserById(USER_ID));
	}

	@Test
	public void testGetUserByIdNullUserId() {
		Long userId = null;

		assertThrows(UserNotFoundException.class, () -> userService.readUserById(userId));
	}

	@Test
	public void testReadAllUsersSuccess() {
		List<User> mockUsers = List.of(user, user);
		when(userRepository.findAll()).thenReturn(mockUsers);

		List<UserDto> expectedUserDtos = List.of(userDto, userDto);
		when(modelMapper.map(any(User.class), eq(UserDto.class)))
			.thenReturn(expectedUserDtos.get(0), expectedUserDtos.get(1));

		List<UserDto> actualUserDtos = userService.readAllUsers();

		assertEquals(expectedUserDtos.size(), actualUserDtos.size());
		assertEquals(expectedUserDtos, actualUserDtos);
	}

	@Test
	public void testReadAllUsersEmptyList() {
		when(userRepository.findAll()).thenReturn(Collections.emptyList());

		assertThrows(NoUsersFoundException.class, () -> userService.readAllUsers());
	}

	@Test
	public void testCreateUserSuccess() {
		when(modelMapper.map(userRequest, User.class)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);

		User result = userService.createUser(userRequest);

		assertNotNull(result);
		assertEquals(USER_ID, result.getUserId());
		assertEquals(FIRST_NAME, result.getFirstName());
		assertEquals(LAST_NAME, result.getLastName());
		assertEquals(EMAIL_ADDRESS, result.getEmailAddress());
		assertEquals(BUDGET, result.getBudget());
	}

	@Test
	public void testCreateUserAlreadyExists() {
		when(modelMapper.map(userRequest, User.class)).thenReturn(user);
		doThrow(DataIntegrityViolationException.class).when(userRepository).save(user);

		assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userRequest));
	}

	@Test
	public void testCreateUserNullInput() {
		assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
	}

	@Test
	public void testCreateRandomUsersInvalidCount() {
		int count = 0;

		assertThrows(IllegalArgumentException.class, () -> userService.createRandomUsers(count));
	}

	@Test
	public void testCreateRandomUsersExceedsLimit() {
		int count = 11;

		assertThrows(IllegalArgumentException.class, () -> userService.createRandomUsers(count));
	}

}
