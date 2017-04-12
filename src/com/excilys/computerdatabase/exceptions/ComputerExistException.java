package com.excilys.computerdatabase.exceptions;

public class ComputerExistException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7221680426615401995L;

	public ComputerExistException() {
		super("Computer Already Exist");
	}

}
