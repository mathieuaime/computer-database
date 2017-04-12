package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface ICompanyDAO {
	List<Company> listCompanies();
	List<Company> listCompanies(int offset, int length);
	Company getCompany(int id);
	boolean addCompany(int id, String name);
	boolean updateCompany(int id, String name);
	boolean deleteCompany(int id);
	List<Computer> getComputers(int id);
}
