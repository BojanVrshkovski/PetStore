package com.petstore.service;

import com.petstore.entity.dto.UserDto;

public interface UserService {
	UserDto readUserById(Long userId);
}
