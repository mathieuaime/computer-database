package service;

import java.util.List;

import model.Company;

import daos.CompanyDAO;

public class CompanyService {
	
	private CompanyDAO companyDao;
	
	public CompanyService() {
		companyDao = new CompanyDAO();
	}
	
	public List<Company> get() {
		return companyDao.listCompanies();
	}
	
	public Company get(int id) {
		return companyDao.getCompany(id);
	}
	
	public void add(Company company) {
		companyDao.addCompany(company.getId(), company.getName());
	}
	
	public void update(int id, Company company) {
		companyDao.updateCompany(id, company.getName());
	}
	
	public void delete(int id) {
		companyDao.deleteCompany(id);
	}

}
