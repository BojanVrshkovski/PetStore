package com.petstore.exception;

public class PetNotFoundException extends RuntimeException{
	public PetNotFoundException() {
		super("The pet you are looking for is not found");
	}
}
