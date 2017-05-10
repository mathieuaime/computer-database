package com.excilys.computerdatabase.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.impl.ComputerServiceImpl;

public class ComputerServiceTest extends DatabaseTestCase {

    private ComputerServiceImpl computerService = ComputerServiceImpl.INSTANCE;
    private Company comp1;
    private Computer c1;
    private Computer c2;

    private static final String SAMPLE_TEST_XML = "src/test/resources/db-sample.xml";

    private static final String URL = Config.getProperties().getProperty("urlTest");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    /**
     * ComputerTest constructor.
     */
    public ComputerServiceTest() {

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
            assertEquals(1, computerService.getById(1L).getId());
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Found");
        }
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetByIdNonPresent() {
        try {
            computerService.getById(1000L);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
        }
    }

    /**
     * Test get by name with zero value.
     */
    @Test
    public void testGetByNameZeroValue() {
        assertEquals(0, computerService.getByName("Computer1000").size());
    }

    /**
     * Test get by name with one value.
     */
    @Test
    public void testGetByNameOneValue() {
        assertEquals(1, computerService.getByName("Computer1").size());
    }

    /**
     * Test get by name with many value.
     */
    @Test
    public void testGetByNameManyValue() {
        assertEquals(2, computerService.getByName("Computer2").size());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPage() {
        assertEquals(4, computerService.getPage().getObjectNumber());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPageWithLimit() {
        assertEquals(2, computerService.getPage(1, 2).getObjectNumber());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPageWithSearch() {
        assertEquals(2, computerService.getPage(1, 2, "Compu", null, null).getObjectNumber());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPageWithSearchNotFound() {
        assertEquals(0, computerService.getPage(1, 2, "Compurdg", null, null).getObjectNumber());
    }

    /**
     * Test add.
     */
    @Test
    public void testAdd() {
        
        computerService.add(c1);

        try {
            Computer c = ComputerMapper.createBean(computerService.getById(c1.getId()));
            assertEquals(c1, c);
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Added");
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }

    /**
     * Test add.
     */
    @Test
    public void testAddNonPresentCompany() {
        
        computerService.add(c1);

        try {
            ComputerDTO c = computerService.getById(c1.getId());
            c.getCompany().setId(1000L);
            ComputerMapper.createBean(c);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Added");
        } catch (CompanyNotFoundException e) {
        }
    }

    /**
     * Test update.
     */
    @Test
    public void testUpdate() {
        
        c1.setId(1L);
        try {
            computerService.update(c1);
        } catch (ComputerNotFoundException e1) {
            fail("Computer Not Found");
        }

        try {
            Computer c = ComputerMapper.createBean(computerService.getById(c1.getId()));
            assertEquals(c1, c);
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Updated");
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }
    
    /**
     * Test update.
     */
    @Test
    public void testUpdateNonPresent() {

        c1.setId(1500L);
        try {
            computerService.update(c1);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
        }
    }

    /**
     * Test delete.
     */
    @Test
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
    }

    /**
     * Test delete non present computer.
     */
    @Test
    public void testDeleteNonPresent() {
        try {
            computerService.delete(1500L);
            fail("Exception Not Thrown");
        } catch (Exception e) {
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void testDeleteList() {
        try {
            computerService.delete(new ArrayList<Long>(Arrays.asList(1L, 2L)));
            computerService.getById(1L);
            computerService.getById(2L);
            fail("Computer Not Deleted");
        } catch (Exception e) {
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void testDeleteListNotFound() {
        try {
            computerService.delete(new ArrayList<Long>(Arrays.asList(1L, 1500L)));
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
            try {
                computerService.getById(1L);
                computerService.getById(2L);
            } catch (ComputerNotFoundException e1) {
                fail("Computer Deleted");
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
        } catch (ComputerNotFoundException e) {
            fail("Computer Not Found");
        } catch (CompanyNotFoundException e) {
            fail("Company Not Found");
        }
    }

    /**
     * Test getCompany when the computer does not exist.
     */
    @Test
    public void testGetCompanyComputerNonPresent() {
        try {
            computerService.getCompany(1500L);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
        } catch (CompanyNotFoundException e) {
            fail("Bad Exception Thrown");
        }
    }

    /**
     * Test getCompany when the company does not exist.
     */
    @Test
    public void testGetCompanyCompanyNonPresent() {
        try {
            computerService.getCompany(3L);
            fail("Exception Not Thrown");
        } catch (ComputerNotFoundException e) {
            fail("Bad Exception Thrown");
        } catch (CompanyNotFoundException e) {
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
