package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import service.CompanyService;

import daos.CompanyDAO;
import model.Company;

public class CompanyTest {

	@Test
	public void test() {
		CompanyService cdao = new CompanyService();
		Company c1 = new Company(100, "Company1");
		Company c2 = new Company(101, "Company2");
		
		try{
		    
			cdao.add(c1);
			cdao.add(c2);

		    assertEquals(c1, cdao.get(100));
		    assertEquals(c2, cdao.get(101));


		    cdao.update(100, c2);
		    cdao.update(101, c1);

		    assertEquals("Company1", cdao.get(101).getName());
		    assertEquals("Company2", cdao.get(100).getName());

		    
		    cdao.delete(100);
		    cdao.delete(101);

		    assertNull(cdao.get(100));
		    assertNull(cdao.get(101));

		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}

}
