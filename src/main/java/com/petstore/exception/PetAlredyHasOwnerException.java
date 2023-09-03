package com.petstore.exception;

public class PetAlredyHasOwnerException extends RuntimeException{
	public PetAlredyHasOwnerException(String message) {
		super(message);
	}
}
