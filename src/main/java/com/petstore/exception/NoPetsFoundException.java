package com.petstore.exception;

public class NoPetsFoundException extends RuntimeException{
	public NoPetsFoundException(String message) {
		super(message);
	}
}
