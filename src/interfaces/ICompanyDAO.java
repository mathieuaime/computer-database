package interfaces;

import java.util.List;

import model.Company;
import model.Computer;

public interface ICompanyDAO {
	List<Company> listCompanies();
	List<Company> listCompanies(int offset, int length);
	Company getCompany(int id);
	boolean addCompany(int id, String name);
	boolean updateCompany(int id, String name);
	boolean deleteCompany(int id);
	List<Computer> getComputers(int id);
}
