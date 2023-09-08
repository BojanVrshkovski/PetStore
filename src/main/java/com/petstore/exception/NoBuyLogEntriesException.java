package com.petstore.exception;

public class NoBuyLogEntriesException extends RuntimeException{
	public NoBuyLogEntriesException(String message) {
		super(message);
	}
}
