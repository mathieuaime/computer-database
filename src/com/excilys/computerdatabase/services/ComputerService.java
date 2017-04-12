package com.excilys.computerdatabase.services;

import java.util.List;

import com.excilys.computerdatabase.daos.ComputerDAOImpl;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.interfaces.PageServ;
import com.excilys.computerdatabase.interfaces.ComputerServ;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerService implements ComputerServ, PageServ<Computer> {

	private ComputerDAOImpl computerDao;

	public ComputerService() {
		computerDao = new ComputerDAOImpl();
	}

	@Override
	public List<Computer> get() {
		return computerDao.findAll();
	}

	@Override
	public Page<Computer> getPage() {
		List<Computer> l = computerDao.findAll();
		return new Page<Computer>(l, 1);
	}

	@Override
	public Page<Computer> getPage(int pageNumero, int length) {
		return new Page<Computer>(computerDao.findAll(pageNumero * length, length), pageNumero);
	}

	@Override
	public Computer get(int id) {
		return computerDao.getById(id);
	}

	@Override
	public boolean add(Computer computer) {
		return computerDao.add(computer);
	}

	@Override
	public boolean update(Computer computer) {
		return computerDao.update(computer);
	}

	@Override
	public boolean delete(int id) {
		return computerDao.delete(id);
	}

	public Company getCompany(int id) {
		return computerDao.getCompany(id);
	}
}
