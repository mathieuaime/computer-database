package com.excilys.computerdatabase.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.slf4j.Logger;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.ComputerServiceImpl;

public class ComputerServiceTest extends DatabaseTestCase {

    private ComputerServiceImpl computerService;
    private Company comp1;
    private Computer c1;
    private Computer c2;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerServiceTest.class);

    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    /**
     * ComputerTest constructor.
     */
    public ComputerServiceTest() {
        computerService = new ComputerServiceImpl();

        comp1 = new Company.Builder("Apple Inc.").id(1000L).build();

        c1 = new Computer("Computer1");
        c1.setId(1000L);
        c1.setCompany(comp1);

        c2 = new Computer("Computer2");
        c2.setId(1001L);
        c2.setCompany(comp1);
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetById() {

        try {

            assertEquals(1, computerService.getById(1L).getId());

            assertNull(computerService.getById(1000L));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test get by name.
     */
    @Test
    public void testGetByName() {

        try {

            assertEquals(1, computerService.getByName("Computer1").size());
            assertEquals(1, computerService.getByName("Computer1").get(0).getId());

            assertEquals(2, computerService.getByName("Computer2").size());

            assertEquals(0, computerService.getByName("Computer1000").size());

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
    public void testGetAll() {
        assertEquals(4, computerService.get().size());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPage() {
        assertEquals(2, computerService.getPage(1, 2).getObjectNumber());
    }

    /**
     * Test add.
     */
    @Test
    public void testAdd() {

        try {            
            computerService.add(c1);

            assertEquals(c1, computerService.getById(c1.getId()));

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
            c1.setId(1);
            computerService.update(c1);

            assertEquals(c1, computerService.getById(c1.getId()));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void testDelete() {
        try {
            
            assertEquals(1L, computerService.getById(1L).getId());

            computerService.delete(1L);

            assertNull(computerService.getById(1L));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void testDeleteList() {
        try {
            
            assertEquals(1L, computerService.getById(1L).getId());
            assertEquals(2L, computerService.getById(2L).getId());

            computerService.delete(new ArrayList<Long>(Arrays.asList(1L, 2L)));

            assertNull(computerService.getById(1L));
            assertNull(computerService.getById(2L));

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

            assertEquals(1L, computerService.getCompany(1L).getId());

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        Connection jdbcConnection = DriverManager.getConnection(URL, USER, PASSWORD);

        return new DatabaseConnection(jdbcConnection);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File(SAMPLE_TEST_XML));

        return dataSet;
    }
}
