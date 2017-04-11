package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import service.ComputerService;

import model.Computer;

public class ComputerTest {
	
	private ComputerService computerService;
	private Computer c1 = new Computer(1000, "Computer1", null, null, 1);
	private Computer c2 = new Computer(1001, "Computer2", null, null, 1);
	
	public ComputerTest() {
		computerService = new ComputerService();
	}
	
	@Before	
    public void executedBeforeEach() {
		computerService.delete(1000);
		computerService.delete(1001);
    }
	
	@Test
	public void testDelete() {		
		try{
		    
			computerService.add(c1);
			
			assertEquals(c1, computerService.get(1000));
		    
		    computerService.delete(1000);

		    assertNull(computerService.get(1000));

		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}

	@Test
	public void testGet() {
		
		try{

		    assertEquals(1, computerService.get(1).getId());
		    
		    assertEquals(574, computerService.get().getObjectNumber());
		    
		    assertEquals(10, computerService.get(5,10).getObjectNumber());
		    
		    assertNull(computerService.get(1000));

		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}
	
	@Test
	public void testAdd() {
		
		try{		    
			computerService.add(c1);

		    assertEquals(c1, computerService.get(1000));
		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}
	
	@Test
	public void testUpdate() {
		
		try{
		    
			computerService.add(c1);
		    
		    computerService.update(1000, c2);

		    assertEquals(c2.getName(), computerService.get(1000).getName());
		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}
	
	@Test
	public void testGetCompany() {
		
		try{

		    assertEquals(1, computerService.getCompany(1).getId());

		    
		  } catch (Exception e) {
			  e.printStackTrace();
		} 
	}
}
