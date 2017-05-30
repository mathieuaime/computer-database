package com.excilys.computerdatabase.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DatabaseTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.config.spring.DAOConfig;
import com.excilys.computerdatabase.config.spring.ServiceConfig;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.services.interfaces.CompanyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, ServiceConfig.class} )
public class CompanyServiceTest extends DatabaseTestCase {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DataSource dataSource;
    
    private IDatabaseTester databaseTester;
    
    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    /**
     * Test get by id.
     */
    @Test
    public void testGetById() {

        try {
            assertEquals(1, companyService.getById(1).getId());
        } catch (Exception e) {
            fail("Company Not Found");
        }
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetByIdNonPresent() {

        try {
            companyService.getById(1000L);
            fail("Exception Not Thrown");
        } catch (NotFoundException e) {
            if (e instanceof CompanyNotFoundException) {
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test get by name.
     */
    @Test
    public void testGetByNameZeroValue() {
        assertEquals(0, companyService.getByName("Company1000").size());
    }

    /**
     * Test get by name.
     */
    @Test
    public void testGetByNameOneValue() {
        assertEquals(1, companyService.getByName("Company1").size());
    }

    /**
     * Test get by name.
     */
    @Test
    public void testGetByNameManyValue() {
        assertEquals(2, companyService.getByName("Company2").size());
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetPage() {
        assertEquals(3, companyService.getPage().getObjectNumber());
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetPageWithLimit() {
        assertEquals(1, companyService.getPage(1, 1).getObjectNumber());
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetPageWithSearch() {
        assertEquals(1, companyService.getPage(1, 1, "Compa", null, null).getObjectNumber());
    }

    /**
     * Test getComputer.
     */
    @Test
    public void testGetComputerZeroValue() {
        try {
            assertEquals(0, companyService.getComputers(3L).size());
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }
    
    /**
     * Test getComputer.
     */
    @Test
    public void testGetComputerOneValue() {
        try {
            assertEquals(1, companyService.getComputers(2L).size());
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }
    
    /**
     * Test getComputer.
     */
    @Test
    public void testGetComputerManyValue() {
        try {
            assertEquals(2, companyService.getComputers(1L).size());
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void testDelete() {
        try {
            companyService.delete(1L);

            assertEquals(0, companyService.getComputers(1L).size());

            try {
                companyService.getById(1L);
                fail("Company Not Deleted");
            } catch (NotFoundException e) {
                if (e instanceof CompanyNotFoundException) {
                } else {
                    fail("Bad Exception Thrown");
                }
            }

        } catch (NotFoundException e) {
            if (e instanceof CompanyNotFoundException) {
                fail("Company Not Found");
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Prepare the test instance by handling the Spring annotations and updating
     * the database to the stale state.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        databaseTester = new DataSourceDatabaseTester(dataSource);
        databaseTester.setDataSet(this.getDataSet());
        databaseTester.setSetUpOperation(this.getSetUpOperation());
        databaseTester.onSetup();
    }

    /**
     * Perform any required database clean up after the test runs to ensure the
     * stale state has not been dirtied for the next test.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        databaseTester.setTearDownOperation(this.getTearDownOperation());
        databaseTester.onTearDown();
    }

    /**
     * Retrieve the DataSet to be used from Xml file. This Xml file should be
     * located on the classpath.
     */
    @Override
    protected IDataSet getDataSet() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File(SAMPLE_TEST_XML));
        return dataSet;
    }
    
    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
    }

    /**
     * On setUp() refresh the database updating the data to the data in the
     * stale state. Cannot currently use CLEAN_INSERT due to foreign key
     * constraints.
     */
    @Override
    protected DatabaseOperation getSetUpOperation() {
        return DatabaseOperation.CLEAN_INSERT;
    }

    /**
     * On tearDown() bring back to the state it was in
     * before the tests started.
     */
    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.NONE;
    }

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        Connection jdbcConnection = DriverManager.getConnection(URL, USER, PASSWORD);

        return new DatabaseConnection(jdbcConnection); 
    }
}
