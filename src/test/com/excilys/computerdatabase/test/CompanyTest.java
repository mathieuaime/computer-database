package test.com.excilys.computerdatabase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.slf4j.Logger;

import main.com.excilys.computerdatabase.services.CompanyService;

public class CompanyTest {

    private CompanyService companyService = new CompanyService();
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyTest.class);

    /**
     * CompanyTest constructor.
     */
    public CompanyTest() {
        companyService = new CompanyService();
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
