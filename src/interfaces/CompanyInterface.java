package interfaces;

import java.util.List;

import model.Company;

public interface CompanyInterface {
	List<Company> listCompanies();
	Company getCompany(int id);
	boolean addCompany(int id, String name);
	boolean updateCompany(int id, String name);
	boolean deleteCompany(int id);
}
