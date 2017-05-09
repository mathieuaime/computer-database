package com.excilys.computerdatabase.daos;

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
import com.excilys.computerdatabase.daos.impl.CompanyDAOImpl;

public class CompanyDAOTest extends DatabaseTestCase {

    private CompanyDAOImpl companyDAO = CompanyDAOImpl.INSTANCE;
    
    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");
    
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyDAOTest.class);

    /**
     * Test get by id.
     */
    @Test
    public void testGetById() {

        try {

            assertEquals(1, companyDAO.getById(1).getId());

            assertNull(companyDAO.getById(1000));

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

            assertEquals(1, companyDAO.getByName("Company1").size());
            assertEquals(1, companyDAO.getByName("Company1").get(0).getId());
            
            assertEquals(2, companyDAO.getByName("Company2").size());

            assertEquals(0, companyDAO.getByName("Company1000").size());

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
    public void testFindAll() {

            assertEquals(3, companyDAO.findAll().size());
    }

    /**
     * Test get by id.
     */
    @Test
    public void testFind() {

            assertEquals(1, companyDAO.findAll(1, 1, null).size());
    }

    /**
     * Test getComputer.
     */
    @Test
    public void testGetComputer() {

        try {

            assertEquals(4, companyDAO.getComputers(1).size());

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
            
            assertEquals(1L, companyDAO.getById(1L).getId());

            companyDAO.delete(1L);

            assertEquals(0, companyDAO.getComputers(1L).size());
            
            assertNull(companyDAO.getById(1L));

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
