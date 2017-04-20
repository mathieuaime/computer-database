package com.excilys.computerdatabase.companytest;

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
import com.excilys.computerdatabase.services.CompanyServiceImpl;

public class CompanyTest extends DatabaseTestCase {

    private CompanyServiceImpl companyService = new CompanyServiceImpl();
    
    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");
    
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyTest.class);

    /**
     * CompanyTest constructor.
     */
    public CompanyTest() {
        companyService = new CompanyServiceImpl(URL);
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetById() {

        try {

            assertEquals(1, companyService.getById(1).getId());

            assertNull(companyService.getById(1000));

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

            assertEquals(1, companyService.getByName("Company1").size());
            assertEquals(1, companyService.getByName("Company1").get(0).getId());
            
            assertEquals(2, companyService.getByName("Company2").size());

            assertEquals(0, companyService.getByName("Company1000").size());

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

            assertEquals(3, companyService.get().size());
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetPage() {

            assertEquals(1, companyService.getPage(1, 1).getObjectNumber());
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
