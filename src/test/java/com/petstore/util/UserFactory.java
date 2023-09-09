package com.petstore.util;

import com.petstore.entity.User;
import com.petstore.entity.dto.UserDto;
import com.petstore.entity.request.UserRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

	public static List<User> createDummyUsers(int count) {
		List<User> users = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			User user = new User();
			user.setUserId((long) i);
			user.setFirstName("User" + i);
			user.setLastName("LastName" + i);
			user.setEmailAddress("user" + i + "@example.com");
			user.setBudget(BigDecimal.valueOf(1000 + i * 100));
			users.add(user);
		}
		return users;
	}
}
