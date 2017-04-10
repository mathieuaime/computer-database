package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import service.ComputerService;

import daos.CompanyDAO;
import daos.ComputerDAO;
import model.Company;
import model.Computer;

public class ComputerTest {

	@Test
	public void test() {
		ComputerService cdao = new ComputerService();
		Computer c1 = new Computer(1000, "Computer1", null, null, 1);
		Computer c2 = new Computer(1001, "Computer2", null, null, 1);
		
		try{
		    
			cdao.add(c1);
			cdao.add(c2);

		    assertEquals(c1, cdao.get(1000));
		    assertEquals(c2, cdao.get(1001));

		    cdao.update(1000, c2);
		    cdao.update(1001, c1);

		    assertEquals("Computer1", cdao.get(1001).getName());
		    assertEquals("Computer2", cdao.get(1000).getName());
		    
		    cdao.delete(1000);
		    cdao.delete(1001);

		    assertNull(cdao.get(1000));
		    assertNull(cdao.get(1001));

		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}

}
