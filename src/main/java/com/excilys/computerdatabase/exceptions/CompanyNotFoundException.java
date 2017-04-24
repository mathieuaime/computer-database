package com.excilys.computerdatabase.exceptions;

public class CompanyNotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 4596262789695026102L;

    /**
     *
     * @param message message
     */
    public CompanyNotFoundException(String message) {
        super(message);
    }

    /**
     *
     * @param cause cause
     */
    public CompanyNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message message
     * @param cause cause
     */
    public CompanyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
