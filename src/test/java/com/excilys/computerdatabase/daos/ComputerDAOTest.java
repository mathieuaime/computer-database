package com.excilys.computerdatabase.daos;

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
import com.excilys.computerdatabase.daos.impl.ComputerDAOImpl;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerDAOTest extends DatabaseTestCase {

    private ComputerDAOImpl computerDAO = ComputerDAOImpl.INSTANCE;
    private Company comp1;
    private Computer c1;
    private Computer c2;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerDAOTest.class);

    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    /**
     * ComputerTest constructor.
     */
    public ComputerDAOTest() {

        comp1 = new Company.Builder("Company2").id(2L).build();

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
            ConnectionMySQL.open();
            assertEquals(1, computerDAO.getById(1L).getId());

            assertNull(computerDAO.getById(1000L));
            ConnectionMySQL.close();

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
            ConnectionMySQL.open();
            assertEquals(1, computerDAO.getByName("Computer1").size());
            assertEquals(1, computerDAO.getByName("Computer1").get(0).getId());

            assertEquals(2, computerDAO.getByName("Computer2").size());

            assertEquals(0, computerDAO.getByName("Computer1000").size());
            ConnectionMySQL.close();

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
        ConnectionMySQL.open();
        assertEquals(4, computerDAO.findAll().size());
        ConnectionMySQL.close();
    }

    /**
     * Test getPage.
     */
    @Test
    public void testFind() {
        ConnectionMySQL.open();
        assertEquals(2, computerDAO.findAll(1, 2, null, null, null).size());
        ConnectionMySQL.close();
    }

    /**
     * Test add.
     */
    @Test
    public void testAdd() {

        try {            
            ConnectionMySQL.open();
            computerDAO.add(c1);
            assertEquals(c1, computerDAO.getById(c1.getId()));
            ConnectionMySQL.close();

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
            ConnectionMySQL.open();
            computerDAO.update(c1);
            assertEquals(c1, computerDAO.getById(c1.getId()));
            ConnectionMySQL.close();

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
            ConnectionMySQL.open();
            assertEquals(1L, computerDAO.getById(1L).getId());

            computerDAO.delete(1L);

            assertNull(computerDAO.getById(1L));
            ConnectionMySQL.close();

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
            ConnectionMySQL.open();
            assertEquals(1L, computerDAO.getById(1L).getId());
            assertEquals(2L, computerDAO.getById(2L).getId());

            computerDAO.delete(new ArrayList<Long>(Arrays.asList(1L, 2L)));

            assertNull(computerDAO.getById(1L));
            assertNull(computerDAO.getById(2L));
            ConnectionMySQL.close();

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
            ConnectionMySQL.open();
            assertEquals(1L, computerDAO.getCompany(1L).getId());
            ConnectionMySQL.close();

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
