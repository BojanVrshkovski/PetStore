package com.petstore.service.impl;

import com.petstore.entity.User;
import com.petstore.exception.UserNotFoundException;
import com.petstore.repository.UserRepository;
import com.petstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUserById(Long userId) {
		return this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}
}
