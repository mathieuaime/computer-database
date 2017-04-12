package com.excilys.computerdatabase.exceptions;

public class ComputerNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4596262789695026102L;

	public ComputerNotFoundException() {
	}

	public ComputerNotFoundException(String message) {
		super(message);
	}

	public ComputerNotFoundException(Throwable cause) {
		super(cause);
	}

	public ComputerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
