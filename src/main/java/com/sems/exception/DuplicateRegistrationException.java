package com.sems.exception;

public class DuplicateRegistrationException extends RuntimeException {

	public DuplicateRegistrationException(String msg) {
		super(msg);
	}
}
