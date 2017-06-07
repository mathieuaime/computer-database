package com.excilys.computerdatabase.validators;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import com.excilys.computerdatabase.dtos.ComputerDTO;

public class ComputerDTOValidatorTest {

    private Locale local = LocaleContextHolder.getLocale();

    @Test
    public void testVerificationFormatDate() {
        String correctFr = "13-10-2000";
        String correct2Fr = "2000-12-12";
        String correctAng = "12-20-2001";
        String faux3 = "12-12-2000 12:12:13";
        String faux4 = "2000-12-12 12:12:13";
        LocaleContextHolder.setLocale(Locale.FRENCH);
        assertEquals(true, ComputerDTOValidator.formatDate(correctFr));
        assertEquals(true, ComputerDTOValidator.formatDate(correct2Fr));
        assertEquals(false, ComputerDTOValidator.formatDate(correctAng));
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        assertEquals(false, ComputerDTOValidator.formatDate(correctFr));
        assertEquals(false, ComputerDTOValidator.formatDate(correct2Fr));
        assertEquals(true, ComputerDTOValidator.formatDate(correctAng));
        assertEquals(false, ComputerDTOValidator.formatDate(faux3));
        assertEquals(false, ComputerDTOValidator.formatDate(faux4));

    }

    @After
    public void setLocale() {
        LocaleContextHolder.setLocale(local);
    }

    @Test
    public void testDateLogique() {
        LocalDate dateMin = LocalDate.MIN;
        LocalDate dateMax = LocalDate.MAX;
        ComputerDTO c = new ComputerDTO();
        assertEquals(true, ComputerDTOValidator.dateLogique(dateMin, dateMax, c));
        assertEquals(false, ComputerDTOValidator.dateLogique(dateMax, dateMin, c));
        assertEquals(true, ComputerDTOValidator.dateLogique(dateMin, null, c));
        assertEquals(false, ComputerDTOValidator.dateLogique(null, dateMax, c));
        assertEquals(true, ComputerDTOValidator.dateLogique(null, null, c));
        assertEquals(true, ComputerDTOValidator.dateLogique(dateMax, dateMax, c));
    }

    @Test
    public void testVerificationName() {
        String correct = "name";
        String faux = "name#";
        String faux2 = "";
        String faux3 = null;
        assertEquals(true,ComputerDTOValidator.verificationName(correct));
        assertEquals(false,ComputerDTOValidator.verificationName(faux));
        assertEquals(false,ComputerDTOValidator.verificationName(faux2));
        assertEquals(false,ComputerDTOValidator.verificationName(faux3));
    }

}
