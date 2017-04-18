package com.excilys.computerdatabase.companytest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.slf4j.Logger;

import com.excilys.computerdatabase.services.CompanyServiceImpl;

public class CompanyTest {

    private CompanyServiceImpl companyService = new CompanyServiceImpl();
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyTest.class);

    /**
     * CompanyTest constructor.
     */
    public CompanyTest() {
        companyService = new CompanyServiceImpl();
    }

    /**
     * Test get.
     */
    @Test
    public void testGet() {

        try {

            assertEquals(1, companyService.get(1).getId());

            assertEquals(42, companyService.get().size());

            assertEquals(10, companyService.getPage(1, 10).getObjectNumber());

            assertNull(companyService.get(1000));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test getComputer.
     */
    @Test
    public void testGetComputer() {

        try {

            assertEquals(40, companyService.getComputers(1).size());

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

}
