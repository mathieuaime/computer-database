package service;

import java.util.List;

import model.Company;
import model.Page;
import daos.CompanyDAO;

public class CompanyService {
	
	private CompanyDAO companyDao;
	
	public CompanyService() {
		companyDao = new CompanyDAO();
	}
	
	public Page<Company> get() {
		List<Company> l = companyDao.listCompanies();
		return new Page<Company>(l, 1);
	}
	
	public Page<Company> get(int pageNumero, int length) {
		return new Page<Company>(companyDao.listCompanies(pageNumero * length, length), pageNumero);
	}
	
	public Company get(int id) {
		return companyDao.getCompany(id);
	}
	
	/*public void add(Company company) {
		companyDao.addCompany(company.getId(), company.getName());
	}
	
	public void update(int id, Company company) {
		companyDao.updateCompany(id, company.getName());
	}
	
	public void delete(int id) {
		companyDao.deleteCompany(id);
	}*/

}
