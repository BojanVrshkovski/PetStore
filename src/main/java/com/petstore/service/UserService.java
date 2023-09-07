package com.petstore.service;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;

import java.util.List;

public interface UserService {
	UserDto readUserById(Long userId);
	List<UserDto> readAllUsers();
	User createUser(UserRequest userRequest);
	List<User> createRandomUsers(int count);
}
