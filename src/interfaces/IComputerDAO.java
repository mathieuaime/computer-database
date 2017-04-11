package interfaces;

import java.util.Date;
import java.util.List;

import model.Company;
import model.Computer;

public interface IComputerDAO {
	List<Computer> listComputers();
	List<Computer> listComputers(int min, int max);
	Computer getComputer(int id);
	boolean addComputer(int id, String name, Date introduced, Date discontinued, int companyId);
	boolean updateComputer(int id, String name, Date introduced, Date discontinued, int companyId);
	boolean deleteComputer(int id);
	Company getCompany(int id);
}
