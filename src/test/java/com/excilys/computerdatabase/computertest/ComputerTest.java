package com.excilys.computerdatabase.computertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.services.ComputerServiceImpl;

public class ComputerTest {

    private ComputerServiceImpl computerService;
    private Company comp1;
    private ComputerDTO c1;
    private ComputerDTO c2;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerTest.class);

    /**
     * ComputerTest constructor.
     */
    public ComputerTest() {
        computerService = new ComputerServiceImpl();
        comp1 = new Company.Builder("Apple Inc.").id(1).build();
        
        c1 = new ComputerDTO();
        c1.setName("Computer1");
        c1.setId(1000);
        c1.setCompanyId(comp1.getId());

        c2 = new ComputerDTO();
        c2.setName("Computer2");
        c2.setId(1001);
        c2.setCompanyId(comp1.getId());
    }

    /**
     * Delete the computers used in the test.
     */
    @Before
    public void executedBeforeEach() {
        try {
            computerService.delete(1000);
            computerService.delete(1001);
        } catch (ComputerNotFoundException e) {
            e.printStackTrace();
        }
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
