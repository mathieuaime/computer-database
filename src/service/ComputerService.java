package service;

import java.util.List;

import model.Company;
import model.Computer;
import model.Page;
import daos.ComputerDAO;
import interfaces.IPage;
import interfaces.IService;

public class ComputerService implements IService<Computer>, IPage<Computer> {
	
	private ComputerDAO computerDao;
	
	public ComputerService() {
		computerDao = new ComputerDAO();
	}
	
	@Override
	public Page<Computer> get() {
		List<Computer> l = computerDao.listComputers();
		return new Page<Computer>(l, 1);
	}
	
	@Override
	public Page<Computer> get(int pageNumero, int length) {
		return new Page<Computer>(computerDao.listComputers(pageNumero * length, length), pageNumero);
	}
	
	@Override
	public Computer get(int id) {
		return computerDao.getComputer(id);
	}
	
	@Override
	public boolean add(Computer computer) {
		return computerDao.addComputer(computer.getId(), computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompany_id());
	}
	
	@Override
	public boolean update(int id, Computer computer) {
		return computerDao.updateComputer(id, computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompany_id());
	}
	
	@Override
	public boolean delete(int id) {
		return computerDao.deleteComputer(id);
	}
	
	public Company getCompany(int id) {
		return computerDao.getCompany(id);
	}
}
