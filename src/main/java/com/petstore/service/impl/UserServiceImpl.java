package com.petstore.service.impl;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.exception.UserNotFoundException;
import com.petstore.repository.UserRepository;
import com.petstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
