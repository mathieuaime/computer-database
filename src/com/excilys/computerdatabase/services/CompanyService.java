package com.excilys.computerdatabase.services;

import java.util.List;

import com.excilys.computerdatabase.daos.CompanyDAOImpl;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.interfaces.PageServ;
import com.excilys.computerdatabase.interfaces.CompanyServ;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class CompanyService implements CompanyServ, PageServ<Company> {

	private CompanyDAOImpl companyDao;

	public CompanyService() {
		companyDao = new CompanyDAOImpl();
	}

	@Override
	public List<Company> get() {
		return companyDao.findAll();
	}

	@Override
	public Company get(int id) {
		return companyDao.getById(id);
	}

	@Override
	public Page<Company> getPage() {
		List<Company> l = companyDao.findAll();
		return new Page<Company>(l, 1);
	}

	@Override
	public Page<Company> getPage(int pageNumero, int length) {
		return new Page<Company>(companyDao.findAll(pageNumero * length, length), pageNumero);
	}

	@Override
	public List<Computer> getComputers(int id) {
		return companyDao.getComputers(id);
	}
}
