package com.petstore.service.impl;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.exception.NoUsersFoundException;
import com.petstore.repository.UserRepository;
import com.petstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
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
	public List<UserDto> readAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new NoUsersFoundException("No users found");
		}

		return users.stream()
		            .map(user -> modelMapper.map(user, UserDto.class))
		            .collect(Collectors.toList());
	}
}
