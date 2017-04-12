package com.excilys.computerdatabase.exceptions;

public class IntroducedAfterDiscontinuedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5585053513184680472L;

	public IntroducedAfterDiscontinuedException() {
	}

	public IntroducedAfterDiscontinuedException(String message) {
		super(message);
	}

	public IntroducedAfterDiscontinuedException(Throwable cause) {
		super(cause);
	}

	public IntroducedAfterDiscontinuedException(String message, Throwable cause) {
		super(message, cause);
	}

}
