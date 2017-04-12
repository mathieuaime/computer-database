package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface CompanyServ {
	List<Company> get();
	Company get(int id);
	List<Computer> getComputers(int id);
}
