package com.excilys.computerdatabase.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import com.excilys.computerdatabase.config.spring.BindingConfig;
import com.excilys.computerdatabase.config.spring.DAOConfig;
import com.excilys.computerdatabase.config.spring.ServiceConfig;
import com.excilys.computerdatabase.daos.interfaces.CompanyDAO;
import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.services.impl.CompanyServiceImpl;
import com.excilys.computerdatabase.services.interfaces.CompanyService;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = { BindingConfig.class, DAOConfig.class, ServiceConfig.class })
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyDAO companyDAO;

    @Mock
    private ComputerDAO computerDAO;

    private Company c1;
    private Company c2;
    private Company c3;

    private Computer co1;
    private Computer co2;

    private final List<Company> l1;
    private final List<Company> l2;
    private final List<Company> l3;

    private final List<Computer> lc1;
    private final List<Computer> lc2;

    public CompanyServiceTest() {
        companyService = new CompanyServiceImpl();

        c1 = new Company();
        c1.setName("Company1");
        c1.setId(1000L);

        c2 = new Company();
        c2.setName("Company2");
        c2.setId(1001L);

        c3 = new Company();
        c3.setName("Company2");
        c3.setId(1002L);

        l1 = new ArrayList<>();
        l2 = new ArrayList<>();
        l3 = new ArrayList<>();

        l1.add(c1);

        l2.add(c2);
        l2.add(c3);

        l3.add(c1);
        l3.add(c2);
        l3.add(c3);

        co1 = new Computer("Computer1");
        co1.setId(1000L);
        co1.setCompany(c1);

        co2 = new Computer("Computer2");
        co2.setId(1001L);
        co2.setCompany(c2);

        lc1 = new ArrayList<>();
        lc2 = new ArrayList<>();

        lc1.add(co1);

        lc2.add(co1);
        lc2.add(co2);
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetById() {
        try {
            Mockito.when(companyDAO.getById(1000L)).thenReturn(c1);
            assertEquals(1000L, companyService.getById(1000L).getId());
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
            Mockito.when(companyDAO.getById(100L)).thenThrow(Mockito.mock(CompanyNotFoundException.class));
            companyService.getById(100L);
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
        Mockito.when(companyDAO.getByName("Company1000")).thenReturn(new ArrayList<>());
        assertEquals(0, companyService.getByName("Company1000").size());
    }

    /**
     * Test get by name.
     */
    @Test
    public void testGetByNameOneValue() {
        Mockito.when(companyDAO.getByName("Company1")).thenReturn(l1);
        assertEquals(1, companyService.getByName("Company1").size());
    }

    /**
     * Test get by name.
     */
    @Test
    public void testGetByNameManyValue() {
        Mockito.when(companyDAO.getByName("Company2")).thenReturn(l2);
        assertEquals(2, companyService.getByName("Company2").size());
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetPage() {
        Mockito.when(companyDAO.findAll(Mockito.any(Page.class))).thenReturn(new Page<>(l3, 1, 10));
        assertEquals(3, companyService.getPage().objectNumber());

    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetPageWithLimit() {

        Mockito.when(companyDAO.findAll(Mockito.any(Page.class))).thenReturn(new Page<>(l1, 1, 1));
        assertEquals(1, companyService.getPage(new Page<>(1, 1)).objectNumber());
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetPageWithSearch() {
        Mockito.when(companyDAO.findAll(Mockito.any(Page.class))).thenReturn(new Page<>(l2, 1, 1));
        assertEquals(2, companyService.getPage(new Page<>(1, 1, "Compa", "ASC", "name")).objectNumber());
    }

    /**
     * Test getComputer.
     */
    @Test
    public void testGetComputerZeroValue() {
        try {
            Mockito.when(companyDAO.getComputers(3L)).thenReturn(new ArrayList<>());
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
            Mockito.when(companyDAO.getComputers(2L)).thenReturn(lc1);
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
            Mockito.when(companyDAO.getComputers(1L)).thenReturn(lc2);
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
            Mockito.doNothing().when(companyDAO).delete(1L);
            Mockito.doNothing().when(computerDAO).delete(1L);
            companyService.delete(1L);

        } catch (NotFoundException e) {
            if (e instanceof CompanyNotFoundException) {
                fail("Company Not Found");
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }
}
