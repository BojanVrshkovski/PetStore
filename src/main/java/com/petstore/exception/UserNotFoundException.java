package com.petstore.exception;

public class UserNotFoundException extends RuntimeException{
	public UserNotFoundException() {
		super("The user you are looking for is not found");
	}
}
