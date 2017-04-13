package main.com.excilys.computerdatabase.exceptions;

public class IntroducedAfterDiscontinuedException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 5585053513184680472L;

    /**
     *
     * @param message message
     */
    public IntroducedAfterDiscontinuedException(String message) {
        super(message);
    }

    /**
     *
     * @param cause cause
     */
    public IntroducedAfterDiscontinuedException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message message
     * @param cause cause
     */
    public IntroducedAfterDiscontinuedException(String message, Throwable cause) {
        super(message, cause);
    }

}
