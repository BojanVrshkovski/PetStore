package com.petstore.controller;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;
import com.petstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@QueryMapping
	public UserDto readUserById(@Argument Long id) {
		return userService.readUserById(id);
	}

	@QueryMapping
	public List<UserDto> readAllUsers(){
		return userService.readAllUsers();
	}

	@MutationMapping
	public User createUser(@Argument UserRequest userRequest){
		return userService.createUser(userRequest);
	}
}
