package com.excilys.computerdatabase.validators;

import java.time.LocalDate;

import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.models.Computer;

public class ComputerValidator {

    /**
     * Ensure the presence of a name in the computer.
     * @param computer the computer to validate
     * @throws NameEmptyException when the name is not set
     */
    private static void validateName(Computer computer) throws NameEmptyException {
        String name = computer.getName();

        if (name.equals("")) {
            throw new NameEmptyException("Name Empty");
        }
    }

    /**
     * Ensure that the introduced date is before the discontinued date.
     * @param computer the computer to validate
     * @throws IntroducedAfterDiscontinuedException when the introduced date is after the discontinued date
     */
    private static void validateDate(Computer computer) throws IntroducedAfterDiscontinuedException {
        LocalDate introduced = computer.getIntroduced();
        LocalDate discontinued = computer.getDiscontinued();

        if (introduced != null && discontinued != null && introduced.isAfter(discontinued)) {
            throw new IntroducedAfterDiscontinuedException("Introduced date after Discontinued date");
        }
    }

    /**
     * Validate a computer.
     * @param computer the computer to validate
     * @throws NameEmptyException when the name is not set
     * @throws IntroducedAfterDiscontinuedException when the introduced date is after the discontinued date
     */
    public static void validate(Computer computer) throws NameEmptyException, IntroducedAfterDiscontinuedException {
        validateName(computer);
        validateDate(computer);
    }
}
