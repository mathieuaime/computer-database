package com.excilys.computerdatabase.computertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.ComputerService;

public class ComputerTest {

    private ComputerService computerService;
    private Company comp1;
    private Computer c1;
    private Computer c2;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerTest.class);

    /**
     * ComputerTest constructor.
     */
    public ComputerTest() {
        computerService = new ComputerService();
        comp1 = new Company.Builder(1, "Apple Inc.").build();
        try {
            c1 = new Computer.Builder("Computer1").id(1000).company(comp1).build();
            c2 = new Computer.Builder("Computer2").id(1001).company(comp1).build();
        } catch (IntroducedAfterDiscontinuedException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Delete the computers used in the test.
     */
    @Before
    public void executedBeforeEach() {
        computerService.delete(1000);
        computerService.delete(1001);
    }

    /**
     * Test delete.
     */
    @Test
    public void testDelete() {
        try {

            computerService.add(c1);

            assertEquals(c1, computerService.get(1000));

            computerService.delete(1000);

            assertNull(computerService.get(1000));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test get.
     */
    @Test
    public void testGet() {

        try {

            assertEquals(1, computerService.get(1).getId());

            assertEquals(574, computerService.get().size());

            assertEquals(10, computerService.getPage(5, 10).getObjectNumber());

            assertNull(computerService.get(1000));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test add.
     */
    @Test
    public void testAdd() {

        try {
            computerService.add(c1);

            assertEquals(c1, computerService.get(1000));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test update.
     */
    @Test
    public void testUpdate() {

        try {

            computerService.add(c1);
            c1.setName(c2.getName());
            computerService.update(c1);

            assertEquals(c2.getName(), computerService.get(1000).getName());

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test getCompany.
     */
    @Test
    public void testGetCompany() {

        try {

            assertEquals(1, computerService.getCompany(1).getId());

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }
}
