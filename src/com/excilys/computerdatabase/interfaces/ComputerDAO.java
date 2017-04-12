package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface ComputerDAO {
	List<Computer> findAll();
	List<Computer> findAll(int min, int max);
	Computer getById(int id);
	List<Computer> getByName(String name);
	boolean add(Computer computer);
	boolean update(Computer computer);
	boolean delete(int id);
	Company getCompany(int id);
}
