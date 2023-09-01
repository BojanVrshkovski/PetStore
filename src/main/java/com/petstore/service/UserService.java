package com.petstore.service;

import com.petstore.entity.dto.UserDto;
import java.util.List;

public interface UserService {
	UserDto readUserById(Long userId);
	List<UserDto> readAllUsers();
}
