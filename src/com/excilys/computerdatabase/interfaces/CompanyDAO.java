package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface CompanyDAO {
	List<Company> findAll();
	List<Company> findAll(int offset, int length);
	Company getById(int id);
	List<Company> getByName(String name);
	List<Computer> getComputers(int id);
}
