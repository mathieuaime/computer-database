package com.excilys.computerdatabase.exceptions;

public class NotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8924491129409191450L;

   /**
    *
    * @param message message
    */
   public NotFoundException(String message) {
       super(message);
   }

   /**
    *
    * @param cause cause
    */
   public NotFoundException(Throwable cause) {
       super(cause);
   }

   /**
    *
    * @param message message
    * @param cause cause
    */
   public NotFoundException(String message, Throwable cause) {
       super(message, cause);
   }
}
