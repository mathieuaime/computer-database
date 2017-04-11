package service;

import java.util.List;

import model.Computer;
import model.Page;
import daos.ComputerDAO;

public class ComputerService {
	private ComputerDAO computerDao;
	
	public ComputerService() {
		computerDao = new ComputerDAO();
	}
	
	public Page<Computer> get() {
		List<Computer> l = computerDao.listComputers();
		return new Page<Computer>(l, 1);
	}
	
	public Page<Computer> get(int pageNumero, int length) {
		return new Page<Computer>(computerDao.listComputers(pageNumero * length, length), pageNumero);
	}
	
	public Computer get(int id) {
		return computerDao.getComputer(id);
	}
	
	public void add(Computer computer) {
		computerDao.addComputer(computer.getId(), computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompany_id());
	}
	
	public void update(int id, Computer computer) {
		computerDao.updateComputer(id, computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompany_id());
	}
	
	public void delete(int id) {
		computerDao.deleteComputer(id);
	}

}
