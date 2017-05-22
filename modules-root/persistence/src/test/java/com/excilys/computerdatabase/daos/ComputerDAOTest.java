package com.excilys.computerdatabase.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;

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
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class } )
public class ComputerDAOTest extends DatabaseTestCase {

    @Autowired
    private ComputerDAO computerDAO;

    @Autowired
    private DataSource dataSource;

    private IDatabaseTester databaseTester;
    
    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    private Company comp1;
    private Company comp2;
    private Computer c1;
    private Computer c2;
    private Computer c3;

    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    /**
     * ComputerTest constructor.
     */
    public ComputerDAOTest() {
        comp1 = new Company.Builder("Company2").id(2L).build();
        comp2 = new Company.Builder("Company2").id(1500L).build();

        c1 = new Computer("Computer1");
        c1.setId(1000L);
        c1.setCompany(comp1);

        c2 = new Computer("Computer2");
        c2.setId(1001L);
        c2.setCompany(comp1);
        
        c3 = new Computer("Computer3");
        c3.setId(1002L);
        c3.setCompany(comp2);
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetById() {
        try {
            assertEquals(1, computerDAO.getById(1L).getId());
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Found");
        }
    }

    /**
     * Test get by id.
     */
    /*@Test
    public void testGetByIdNonPresent() {
        try {
            computerService.getById(1000L);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
        }
    }*/

    /**
     * Test get by name with zero value.
     */
    /*@Test
    public void testGetByNameZeroValue() {
        assertEquals(0, computerService.getByName("Computer1000").size());
    }*/

    /**
     * Test get by name with one value.
     */
    /*@Test
    public void testGetByNameOneValue() {
        assertEquals(1, computerService.getByName("Computer1").size());
    }*/

    /**
     * Test get by name with many value.
     */
    /*@Test
    public void testGetByNameManyValue() {
        assertEquals(2, computerService.getByName("Computer2").size());
    }*/

    /**
     * Test getPage.
     */
    /*@Test
    public void testGetPage() {
        assertEquals(4, computerService.getPage().getObjectNumber());
    }*/

    /**
     * Test getPage.
     */
    /*@Test
    public void testGetPageWithLimit() {
        assertEquals(2, computerService.getPage(1, 2).getObjectNumber());
    }*/

    /**
     * Test getPage.
     */
    /*@Test
    public void testGetPageWithSearch() {
        assertEquals(2, computerService.getPage(1, 2, "Compu", null, null).getObjectNumber());
    }*/

    /**
     * Test getPage.
     */
    /*@Test
    public void testGetPageWithSearchNotFound() {
        assertEquals(0, computerService.getPage(1, 2, "Compurdg", null, null).getObjectNumber());
    }*/

    /**
     * Test getCompany.
     */
    /*@Test
    public void testGetCompany() {
        try {
            assertEquals(1L, computerService.getCompany(1L).getId());
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Found");
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }*/

    /**
     * Test getCompany when the computer does not exist.
     */
    /*@Test
    public void testGetCompanyComputerNonPresent() {
        try {
            computerService.getCompany(1500L);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
        } catch (CompanyNotFoundException e) {
            fail("Bad Exception Thrown");
        }
    }*/

    /**
     * Test add.
     */
    /*@Test
    public void testAdd() {

        try {
            computerService.add(c1);
            Computer c = ComputerMapper.createBean(computerService.getById(c1.getId()));
            assertEquals(c1, c);
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Added");
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }*/

    /**
     * Test add.
     */
    /*@Test
    public void testAddNonPresentCompany() {

        try {
            computerService.add(c3);
            fail("Exception Not Thrown");
        } catch (CompanyNotFoundException e) {
        }
    }*/

    /**
     * Test update.
     */
    /*@Test
    public void testUpdate() {

        c1.setId(1L);
        try {
            computerService.update(c1);
        } catch (ComputerNotFoundException e1) {
            fail("Computer Not Found");
        } catch (CompanyNotFoundException e1) {
            fail("Company Not Found");
        }

        try {
            Computer c = ComputerMapper.createBean(computerService.getById(c1.getId()));
            assertEquals(c1, c);
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Updated");
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }*/

    /**
     * Test update.
     */
    /*@Test
    public void testUpdateNonPresent() {

        c1.setId(1500L);
        try {
            computerService.update(c1);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
        } catch (CompanyNotFoundException e1) {
            fail("Bad Exception thrown");
        }
    }*/

    /**
     * Test delete.
     */
    /*@Test
    public void testDelete() {
        try {
            computerService.delete(1L);
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Found");
        }

        try {
            computerService.getById(1L);
            fail("Computer Not Deleted");
        } catch (ComputerNotFoundException e) {
        }
    }*/

    /**
     * Test delete.
     */
    /*@Test
    public void testDeleteList() {
        try {
            computerService.delete(new ArrayList<Long>(Arrays.asList(1L, 2L)));
            computerService.getById(1L);
            computerService.getById(2L);
            fail("Computer Not Deleted");
        } catch (Exception e) {
        }
    }*/


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
