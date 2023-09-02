package com.petstore.exception;

public class PetAlredyExistsException extends RuntimeException{
	public PetAlredyExistsException(String message) {
		super(message);
	}
}
