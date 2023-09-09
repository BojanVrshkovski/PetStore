package com.petstore.util;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;
import static com.petstore.util.UserConstants.USER_ID;
import static com.petstore.util.UserConstants.FIRST_NAME;
import static com.petstore.util.UserConstants.LAST_NAME;
import static com.petstore.util.UserConstants.EMAIL_ADDRESS;
import static com.petstore.util.UserConstants.BUDGET;

public class UserFactory {
	public static UserDto getDefaultUserDto(){
		UserDto userDto = new UserDto(USER_ID,FIRST_NAME,LAST_NAME,EMAIL_ADDRESS,BUDGET);

		return userDto;
	}

	public static User getDefaultUser(){
		User user = new User(USER_ID,FIRST_NAME,LAST_NAME,EMAIL_ADDRESS,BUDGET);

		return user;
	}

	public static UserRequest getDefaultUserRequest(){
		UserRequest userRequest = new UserRequest(FIRST_NAME,LAST_NAME,EMAIL_ADDRESS,BUDGET);

		return userRequest;
	}
}
