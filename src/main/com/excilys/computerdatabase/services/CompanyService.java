package main.com.excilys.computerdatabase.services;

import java.util.List;

import main.com.excilys.computerdatabase.daos.CompanyDAOImpl;
import main.com.excilys.computerdatabase.dtos.Page;
import main.com.excilys.computerdatabase.interfaces.PageServ;
import main.com.excilys.computerdatabase.interfaces.CompanyServ;
import main.com.excilys.computerdatabase.models.Company;
import main.com.excilys.computerdatabase.models.Computer;

public class CompanyService implements CompanyServ, PageServ<Company> {

    private CompanyDAOImpl companyDao;

    /**
     * CompanyService constructor.
     */
    public CompanyService() {
        companyDao = new CompanyDAOImpl();
    }

    @Override
    public List<Company> get() {
        return companyDao.findAll();
    }

    @Override
    public Company get(int id) {
        return companyDao.getById(id);
    }

    @Override
    public Page<Company> getPage() {
        List<Company> l = companyDao.findAll();
        return new Page<Company>(l, 1);
    }

    @Override
    public Page<Company> getPage(int pageNumero, int length) {
        return new Page<Company>(companyDao.findAll(pageNumero * length, length), pageNumero);
    }

    @Override
    public List<Computer> getComputers(int id) {
        return companyDao.getComputers(id);
    }
}
