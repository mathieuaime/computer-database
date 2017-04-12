package com.excilys.computerdatabase.services;

import java.util.List;

import com.excilys.computerdatabase.daos.CompanyDAO;
import com.excilys.computerdatabase.interfaces.IPage;
import com.excilys.computerdatabase.interfaces.IService;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.Page;

public class CompanyService implements IService<Company>, IPage<Company> {
	
	private CompanyDAO companyDao;
	
	public CompanyService() {
		companyDao = new CompanyDAO();
	}
	
	@Override
	public Page<Company> get() {
		List<Company> l = companyDao.listCompanies();
		return new Page<Company>(l, 1);
	}
	
	@Override
	public Page<Company> get(int pageNumero, int length) {
		return new Page<Company>(companyDao.listCompanies(pageNumero * length, length), pageNumero);
	}
	
	@Override
	public Company get(int id) {
		return companyDao.getCompany(id);
	}
	
	@Override
	public boolean add(Company company) {
		return companyDao.addCompany(company.getId(), company.getName());
	}
	
	@Override
	public boolean update(int id, Company company) {
		return companyDao.updateCompany(id, company.getName());
	}
	
	@Override
	public boolean delete(int id) {
		return companyDao.deleteCompany(id);
	}
	
	public List<Computer> getComputers(int id) {
		return companyDao.getComputers(id);
	}
}
