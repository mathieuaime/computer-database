package com.excilys.computerdatabase.exceptions;

public class UserNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 1291570029220364884L;

    /**
     * @param message message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause cause
     */
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message message
     * @param cause cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
