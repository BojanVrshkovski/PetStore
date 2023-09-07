package com.petstore.service.impl;

import com.github.javafaker.Faker;
import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;
import com.petstore.exception.UserAlreadyExistException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.exception.NoUsersFoundException;
import com.petstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import static org.mockito.Mockito.doThrow;
import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userService;
	@Mock
	private ModelMapper modelMapper;

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

	@Test
	public void testReadAllUsersSuccess() {
		User user1 = new User();
		user1.setUserId(1L);
		user1.setFirstName("user1");

		User user2 = new User();
		user2.setUserId(2L);
		user2.setFirstName("user2");

		List<User> mockUsers = List.of(user1, user2);
		when(userRepository.findAll()).thenReturn(mockUsers);

		UserDto userDto1 = new UserDto();
		userDto1.setUserId(1L);
		userDto1.setFirstName("user1");

		UserDto userDto2 = new UserDto();
		userDto2.setUserId(2L);
		userDto2.setFirstName("user2");

		List<UserDto> expectedUserDtos = List.of(userDto1, userDto2);
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
		UserRequest userRequest= new UserRequest( "Bojan", "Vrshkovski", "bojan@gmail.com", new BigDecimal("1000.0"));
		User userEntity = new User();
		userEntity.setUserId(1L);
		userEntity.setFirstName("Bojan");
		userEntity.setLastName("Vrshkovski");
		userEntity.setEmailAddress("bojan@gmail.com");
		userEntity.setBudget(new BigDecimal("1000.0"));

		when(modelMapper.map(userRequest, User.class)).thenReturn(userEntity);
		when(userRepository.save(userEntity)).thenReturn(userEntity);

		User result = userService.createUser(userRequest);

		assertNotNull(result);
		assertEquals(1L, result.getUserId());
		assertEquals("Bojan", result.getFirstName());
		assertEquals("Vrshkovski", result.getLastName());
		assertEquals("bojan@gmail.com", result.getEmailAddress());
		assertEquals(new BigDecimal("1000.0"), result.getBudget());
	}

	@Test
	public void testCreateUserAlreadyExists() {
		UserRequest userRequest= new UserRequest( "Bojan", "Vrshkovski", "bojan@gmail.com", new BigDecimal("1000.0"));
		User userEntity = new User();
		userEntity.setUserId(1L);
		userEntity.setFirstName("Bojan");
		userEntity.setLastName("Vrshkovski");
		userEntity.setEmailAddress("bojan@gmail.com");
		userEntity.setBudget(new BigDecimal("1000.0"));

		when(modelMapper.map(userRequest, User.class)).thenReturn(userEntity);
		doThrow(DataIntegrityViolationException.class).when(userRepository).save(userEntity);

		assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userRequest));
	}

	@Test
	public void testCreateUserNullInput() {
		assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
	}

	@Test
	public void testCreateRandomUsersInvalidCount() {
		int count = 0; // Invalid count

		// Ensure that an IllegalArgumentException is thrown for invalid input
		assertThrows(IllegalArgumentException.class, () -> userService.createRandomUsers(count));
	}

	@Test
	public void testCreateRandomUsersExceedsLimit() {
		int count = 11; // Exceeds the limit

		// Ensure that an IllegalArgumentException is thrown for exceeding the limit
		assertThrows(IllegalArgumentException.class, () -> userService.createRandomUsers(count));
	}

}
