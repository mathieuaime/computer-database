package com.excilys.computerdatabase.exceptions;

public class NameEmptyException extends Exception {
    private static final long serialVersionUID = 3681709208879518921L;

    /**
    * @param message message
    */
   public NameEmptyException(String message) {
       super(message);
   }

   /**
    * @param cause cause
    */
   public NameEmptyException(Throwable cause) {
       super(cause);
   }

   /**
    * @param message message
    * @param cause cause
    */
   public NameEmptyException(String message, Throwable cause) {
       super(message, cause);
   }
}
