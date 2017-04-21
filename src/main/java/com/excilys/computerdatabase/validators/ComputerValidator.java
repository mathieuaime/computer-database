package com.excilys.computerdatabase.validators;

import java.time.LocalDate;

import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.models.Computer;

public class ComputerValidator {

    private static void validateName(Computer computer) throws NameEmptyException {
        
        String name = computer.getName();

        if (name.equals("")) {
            throw new NameEmptyException("Name Empty");
        }

    }

    private static void validateDate(Computer computer) throws IntroducedAfterDiscontinuedException {

        LocalDate introduced = computer.getIntroduced();
        LocalDate discontinued = computer.getDiscontinued();

        if (introduced != null && discontinued != null && introduced.isAfter(discontinued)) {
            throw new IntroducedAfterDiscontinuedException("Introduced date after Discontinued date");
        }
    }
    
    public static void validate(Computer computer) throws NameEmptyException, IntroducedAfterDiscontinuedException {
        validateName(computer);
        validateDate(computer);
    }
}
