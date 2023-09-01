package com.petstore.exception;

public class NoUsersFoundException extends RuntimeException{
	public NoUsersFoundException(String message) {
		super(message);
	}
}
