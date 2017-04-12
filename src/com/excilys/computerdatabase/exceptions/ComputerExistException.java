package com.excilys.computerdatabase.exceptions;

public class ComputerExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7221680426615401995L;

	public ComputerExistException() {
	}

	public ComputerExistException(String message) {
		super(message);
	}

	public ComputerExistException(Throwable cause) {
		super(cause);
	}

	public ComputerExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
