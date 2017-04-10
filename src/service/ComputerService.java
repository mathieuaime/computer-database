package service;

import java.util.List;

import model.Computer;
import daos.ComputerDAO;

public class ComputerService {
	private ComputerDAO computerDao;
	
	public ComputerService() {
		computerDao = new ComputerDAO();
	}
	
	public List<Computer> get() {
		return computerDao.listComputers();
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
