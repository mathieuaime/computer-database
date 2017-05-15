package com.excilys.computerdatabase.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.services.interfaces.CompanyService;

public class CompanyServiceTest extends DatabaseTestCase {

    private CompanyService companyService;

    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    public CompanyServiceTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.excilys.computerdatabase");
        context.refresh();

        companyService = (CompanyService) context.getBean("companyService");

        context.close();
    }

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
        } catch (CompanyNotFoundException e) {
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
            } catch (CompanyNotFoundException e) {
            }

        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
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

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT; // by default (will do DELETE_ALL
                                               // + INSERT)
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE; // by default
    }

}
