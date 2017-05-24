package com.excilys.computerdatabase.exceptions;

public class RoleNotFoundException extends Exception {
    private static final long serialVersionUID = -5189006088297552567L;

    /**
     * @param message message
     */
    public RoleNotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause cause
     */
    public RoleNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message message
     * @param cause cause
     */
    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
