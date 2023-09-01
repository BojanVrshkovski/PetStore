package com.petstore.controller;

import com.petstore.entity.dto.UserDto;
import com.petstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@QueryMapping
	UserDto readUserById(@Argument Long id){
		return userService.readUserById(id);
	}
}
