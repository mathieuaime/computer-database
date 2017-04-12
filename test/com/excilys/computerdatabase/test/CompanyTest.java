package com.excilys.computerdatabase.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.services.CompanyService;

public class CompanyTest {
	
	private CompanyService companyService = new CompanyService();
	private Company c1 = new Company.Builder(1000, "Company1").build();
	private Company c2 = new Company.Builder(1001, "Company2").build();
	
	public CompanyTest() {
		companyService = new CompanyService();
	}

	@Test
	public void testGet() {
		
		try{

		    assertEquals(1, companyService.get(1).getId());
		    
		    assertEquals(42, companyService.get().size());
		    
		    assertEquals(10, companyService.getPage(1,10).getObjectNumber());
		    
		    assertNull(companyService.get(1000));

		    
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
