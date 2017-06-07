package com.excilys.computerdatabase.validators;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test
    public void testRegex () {
        String date = "01/02/2000";
        String date1 = "01-02-2000";
        String date2 = "1999-01-01";
        String date3 = "12-20-1999";
        String regexDateFrancaise ="^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-[0-9]{4}";
        String regexDateFrancaise2 ="^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])";
        String regexDateAnglaise3 = "^(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])-[0-9]{4}";
        Pattern p = Pattern.compile(regexDateFrancaise);
        Pattern p2 = Pattern.compile(regexDateFrancaise2);
                
        Matcher m = p.matcher(date1);
        Matcher m2 = p.matcher(date);
        if(!m.matches()) {
            fail();
        }
        if(m2.matches()) {
            fail();
        }
        if(!p2.matcher(date2).matches()) {
            fail();
        }
        if(!Pattern.matches(regexDateAnglaise3, date3)) {
            fail();
        }
      }
}
