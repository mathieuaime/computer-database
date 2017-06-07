package com.excilys.computerdatabase.validators;

import static org.junit.Assert.*;

import org.junit.Test;

import com.excilys.computerdatabase.models.Page;


public class PageValidatorTest {

    @Test
    public void testVerificationNbElement() {
        int correct = 50;
        int faux = 12;
        assertEquals(true,PageValidator.verificationPageSize(correct));
        assertEquals(false,PageValidator.verificationPageSize(faux));
    }


    @Test
    public void testVerificationTypeSearch() {
        String correct  = "company.name";
        String correct2 = "name";
        String faux = "Coamp";;
        assertEquals(true, PageValidator.verificationColumn(correct));
        assertEquals(true, PageValidator.verificationColumn(correct2));
        assertEquals(false, PageValidator.verificationColumn(faux));
    }
    
    @Test
    public void testVerificationValidator() {
        boolean correct2 =  PageValidator.verificationSearch("");
        boolean correct3 =  PageValidator.verificationSearch("name");
        boolean faux  =  PageValidator.verificationSearch("name#");
        assertEquals(true,correct2);
        assertEquals(true,correct3);
        assertEquals(false,faux);
    }
    
    @Test
    public void testAttributVide () {
        Page<?> navigation = new Page<>();
        boolean correct  = PageValidator.attributVide(navigation);
        navigation.setOrder(null);
        boolean faux = PageValidator.attributVide(navigation);
        navigation.setOrder("la");
        navigation.setColumn(null);
        boolean faux2 = PageValidator.attributVide(navigation);
        assertEquals(correct,false);
        assertEquals(faux,true);
        assertEquals(faux2,true);
    }
}
