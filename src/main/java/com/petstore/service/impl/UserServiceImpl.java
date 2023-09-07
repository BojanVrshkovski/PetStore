package com.petstore.service.impl;

import com.github.javafaker.Faker;
import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;
import com.petstore.exception.UserAlreadyExistException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.exception.NoUsersFoundException;
import com.petstore.repository.UserRepository;
import com.petstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;


	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDto readUserById(Long userId) {
		log.info(String.format("User with id: %d is being fetched", userId));
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> readAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			log.error(String.format("No users found"));
			throw new NoUsersFoundException("No users found");
		}
		log.info(String.format("Retriving all the users"));
		return users.stream()
		            .map(user -> modelMapper.map(user, UserDto.class))
		            .collect(Collectors.toList());
	}

	@Override
	public User createUser(UserRequest userRequest) {
		if (userRequest == null) {
			throw new IllegalArgumentException("userRequest is null");
		}
		User user;
		try {
			user = modelMapper.map(userRequest, User.class);
			user = userRepository.save(user);
			log.info(
				String.format("User successfully added in database with email address: %s", userRequest.getEmailAddress()));
		} catch (DataIntegrityViolationException e) {
			log.error(String.format("User with that email alredy exists"));
			throw new UserAlreadyExistException("User with that email alredy exists");
		}
		return user;
	}

	public List<User> createRandomUsers(int count) {
		if (count <= 0 || count > 10) {
			throw new IllegalArgumentException("Count should be between 1 and 10");
		}

		List<User> createdUsers = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			UserRequest randomUserRequest = generateRandomUserData();
			User createdUser = createUser(randomUserRequest);
			createdUsers.add(createdUser);
		}

		return createdUsers;
	}

	public UserRequest generateRandomUserData() {
		Faker faker = new Faker();

		UserRequest userRequest = new UserRequest();
		userRequest.setFirstName(faker.name().firstName());
		userRequest.setLastName(faker.name().lastName());
		userRequest.setEmailAddress(faker.internet().emailAddress());
		BigDecimal minBudget = new BigDecimal("100");
		BigDecimal maxBudget = new BigDecimal("1000");
		BigDecimal range = maxBudget.subtract(minBudget);
		double scaledRandom = Math.random() * range.doubleValue() + minBudget.doubleValue();
		BigDecimal randomBudget = BigDecimal.valueOf(scaledRandom);
		userRequest.setBudget(randomBudget);

		return userRequest;
	}
}
