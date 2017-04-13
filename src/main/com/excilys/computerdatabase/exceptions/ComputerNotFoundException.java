package main.com.excilys.computerdatabase.exceptions;

public class ComputerNotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 4596262789695026102L;

    /**
     *
     * @param message message
     */
    public ComputerNotFoundException(String message) {
        super(message);
    }

    /**
     *
     * @param cause cause
     */
    public ComputerNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message message
     * @param cause cause
     */
    public ComputerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
