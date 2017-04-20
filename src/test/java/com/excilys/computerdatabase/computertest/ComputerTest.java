package com.excilys.computerdatabase.computertest;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.slf4j.Logger;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.services.ComputerServiceImpl;

public class ComputerTest extends DatabaseTestCase {

    private ComputerServiceImpl computerService;
    private Company comp1;
    private ComputerDTO c1;
    private ComputerDTO c2;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerTest.class);

    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    /**
     * ComputerTest constructor.
     */
    public ComputerTest() {
        computerService = new ComputerServiceImpl(URL);

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
     * Test get by id.
     */
    @Test
    public void testGetById() {

        try {

            assertEquals(1, computerService.getById(1).getId());

            assertNull(computerService.getById(1000));

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

            computerService.delete(1);

            assertNull(computerService.getById(1));

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
