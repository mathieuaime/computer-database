package interfaces;

import java.sql.Timestamp;
import java.util.List;

import model.Computer;

public interface IComputerDAO {
	List<Computer> listComputers();
	List<Computer> listComputers(int min, int max);
	Computer getComputer(int id);
	boolean addComputer(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId);
	boolean updateComputer(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId);
	boolean deleteComputer(int id);
}
