package com.excilys.computerdatabase.exceptions;

public class ComputerExistException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 7221680426615401995L;

    /**
     *
     * @param message message
     */
    public ComputerExistException(String message) {
        super(message);
    }

    /**
     *
     * @param cause cause
     */
    public ComputerExistException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message message
     * @param cause cause
     */
    public ComputerExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
