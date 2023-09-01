package com.petstore.service;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import java.util.List;

public interface UserService {
	List<UserDto> readAllUsers();
}
