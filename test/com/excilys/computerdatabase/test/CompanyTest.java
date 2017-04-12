package com.excilys.computerdatabase.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.services.CompanyService;

public class CompanyTest {
	
	private CompanyService companyService = new CompanyService();
	private Company c1 = new Company(1000, "Company1");
	private Company c2 = new Company(1001, "Company2");
	
	public CompanyTest() {
		companyService = new CompanyService();
	}
	
	@Before	
    public void executedBeforeEach() {
		companyService.delete(1000);
		companyService.delete(1001);
    }
	
	@Test
	public void testDelete() {		
		try{
		    
			companyService.add(c1);
			
			assertEquals(c1, companyService.get(1000));
		    
		    companyService.delete(1000);

		    assertNull(companyService.get(1000));

		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}

	@Test
	public void testGet() {
		
		try{

		    assertEquals(1, companyService.get(1).getId());
		    
		    assertEquals(42, companyService.get().getObjectNumber());
		    
		    assertEquals(10, companyService.get(1,10).getObjectNumber());
		    
		    assertNull(companyService.get(1000));

		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}
	
	@Test
	public void testAdd() {
		
		try{		    
			companyService.add(c1);

		    assertEquals(c1, companyService.get(1000));
		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}
	
	@Test
	public void testUpdate() {
		
		try{
		    
			companyService.add(c1);
		    
		    companyService.update(1000, c2);

		    assertEquals(c2.getName(), companyService.get(1000).getName());
		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}
	
	@Test
	public void testGetComputer() {
		
		try{

		    assertEquals(40, companyService.getComputers(1).size());
		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}

}
