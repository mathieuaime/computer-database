package com.excilys.computerdatabase.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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
import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.services.impl.ComputerServiceImpl;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = { BindingConfig.class, DAOConfig.class, ServiceConfig.class} )
public class ComputerServiceTest {

    @InjectMocks
    private ComputerService computerService;

    @Mock
    private ComputerDAO computerDAO;

    private final Company comp1;
    private final Company comp2;
    private final Computer c1;
    private final Computer c2;
    private final Computer c3;
    
    private final List<Computer> l1;
    private final List<Computer> l2;
    private final List<Computer> l3;

    /**
     * ComputerTest constructor.
     */
    public ComputerServiceTest() {

        computerService = new ComputerServiceImpl();

        comp1 = new Company.Builder("Company2").id(2L).build();
        comp2 = new Company.Builder("Company2").id(1500L).build();

        c1 = new Computer("Computer1");
        c1.setId(1000L);
        c1.setCompany(comp1);

        c2 = new Computer("Computer2");
        c2.setId(1001L);
        c2.setCompany(comp1);
        
        c3 = new Computer("Computer2");
        c3.setId(1002L);
        c3.setCompany(comp2);
        
        l1 = new ArrayList<>();
        l2 = new ArrayList<>();
        l3 = new ArrayList<>();
        
        l1.add(c1);
        
        l2.add(c2);
        l2.add(c3);
        
        l3.add(c1);
        l3.add(c2);
        l3.add(c3);
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetById() {
        try {
            Mockito.when(computerDAO.getById(1L)).thenReturn(c1);
            assertEquals(1000L, computerService.getById(1L).getId());
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
                fail("Computer Not Found");
            } else if (e instanceof CompanyNotFoundException) {
                fail("Company Not Found");
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test get by id.
     */
    @Test
    public void testGetByIdNonPresent() {
        try {
            Mockito.when(computerDAO.getById(1000L)).thenThrow(Mockito.mock(ComputerNotFoundException.class));
            computerService.getById(1000L);
            fail("Exception Not Thrown");
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test get by name with zero value.
     */
    @Test
    public void testGetByNameZeroValue() {
        Mockito.when(computerDAO.getByName("Computer1000")).thenReturn(new ArrayList<>());
        assertEquals(0, computerService.getByName("Computer1000").size());
    }

    /**
     * Test get by name with one value.
     */
    @Test
    public void testGetByNameOneValue() {
        Mockito.when(computerDAO.getByName("Computer1")).thenReturn(l1);
        assertEquals(1, computerService.getByName("Computer1").size());
    }

    /**
     * Test get by name with many value.
     */
    @Test
    public void testGetByNameManyValue() {
        Mockito.when(computerDAO.getByName("Computer2")).thenReturn(l2);
        assertEquals(2, computerService.getByName("Computer2").size());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPage() {
        Mockito.when(computerDAO.findAll(Mockito.any(Page.class))).thenReturn(new Page<>(l3, 1, 10));
        assertEquals(3, computerService.getPage().objectNumber());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPageWithLimit() {
        Mockito.when(computerDAO.findAll(Mockito.any(Page.class))).thenReturn(new Page<>(l2, 1, 2));
        assertEquals(2, computerService.getPage(new Page<>(1, 2)).objectNumber());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPageWithSearch() {
        Mockito.when(computerDAO.findAll(Mockito.any(Page.class))).thenReturn(new Page<>(l2, 1, 2));
        assertEquals(2, computerService.getPage(new Page<>(1, 2, "Compu", "ASC", "name")).objectNumber());
    }

    /**
     * Test getPage.
     */
    @Test
    public void testGetPageWithSearchNotFound() {
        Mockito.when(computerDAO.findAll(Mockito.any(Page.class))).thenReturn(new Page<>(1, 2));
        assertEquals(0, computerService.getPage(new Page<>(1, 2, "Compurdg", "ASC", "name")).objectNumber());
    }
    

    /**
     * Test getCompany.
     */
    @Test
    public void testGetCompany() {
        try {
            Mockito.when(computerDAO.getCompany(1L)).thenReturn(comp1);
            assertEquals(2L, computerService.getCompany(1L).getId());
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
                fail("Computer Not Found");
            } else if (e instanceof CompanyNotFoundException) {
                fail("Company Not Found");
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test getCompany when the computer does not exist.
     */
    @Test
    public void testGetCompanyComputerNonPresent() {
        try {
            Mockito.when(computerDAO.getCompany(1500L)).thenThrow(Mockito.mock(ComputerNotFoundException.class));
            computerService.getCompany(1500L);
            fail("Exception Not Thrown");
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test add.
     */
    @Test
    public void testAdd() {
        try {
            Mockito.when(computerDAO.save(c1)).thenReturn(c1);
            Computer c = computerService.save(c1);
            assertEquals(c1, c);
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
                fail("Computer Not Found");
            } else if (e instanceof CompanyNotFoundException) {
                fail("Company Not Found");
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test add.
     */
    @Test
    public void testAddNonPresentCompany() {

        try {
            Mockito.when(computerDAO.save(c3)).thenThrow(Mockito.mock(CompanyNotFoundException.class));
            computerService.save(c3);
            fail("Exception Not Thrown");
        } catch (NotFoundException e) {
            if (e instanceof CompanyNotFoundException) {
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test update.
     */
    @Test
    public void testUpdate() {

        c1.setId(1L);
        try {
            Mockito.when(computerDAO.update(c1)).thenReturn(c1);
            Computer c = computerService.update(c1);
            assertEquals(c1, c);
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
                fail("Computer Not Found");
            } else if (e instanceof CompanyNotFoundException) {
                fail("Company Not Found");
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test update.
     */
    @Test
    public void testUpdateNonPresent() {

        c1.setId(1500L);
        try {
            Mockito.when(computerDAO.update(c1)).thenThrow(Mockito.mock(ComputerNotFoundException.class));
            computerService.update(c1);
            fail("Exception Not Thrown");
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void testDelete() {
        try {
            Mockito.doNothing().when(computerDAO).delete(1L);
            computerService.delete(1L);
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
                fail("Computer Not Found");
            } else {
                fail("Bad Exception Thrown");
            }
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void testDeleteList() {
        try {
            Mockito.doNothing().when(computerDAO).delete(new ArrayList<>(Arrays.asList(1L, 2L)));
            computerService.delete(new ArrayList<>(Arrays.asList(1L, 2L)));
        } catch (NotFoundException e) {
            fail("Computer Not Deleted");
        }
    }
    
    @Before
    public void setup() {
    }
}
