package com.petstore.service.impl;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;
import com.petstore.exception.UserAlreadyExistException;
import com.petstore.exception.UserNotFoundException;
import com.petstore.exception.NoUsersFoundException;
import com.petstore.repository.UserRepository;
import com.petstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}


	@Override
	public UserDto readUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user,UserDto.class);
	}
	@Override
	public List<UserDto> readAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new NoUsersFoundException("No users found");
		}

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
		}catch (DataIntegrityViolationException e){
			throw new UserAlreadyExistException("User with that email alredy exists");
		}
		return user;
	}
}
