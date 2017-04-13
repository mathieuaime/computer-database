package main.com.excilys.computerdatabase.services;

import java.util.List;

import main.com.excilys.computerdatabase.daos.ComputerDAOImpl;
import main.com.excilys.computerdatabase.dtos.Page;
import main.com.excilys.computerdatabase.interfaces.PageServ;
import main.com.excilys.computerdatabase.interfaces.ComputerServ;
import main.com.excilys.computerdatabase.models.Company;
import main.com.excilys.computerdatabase.models.Computer;

public class ComputerService implements ComputerServ, PageServ<Computer> {

    private ComputerDAOImpl computerDao;

    /**
     * ComputerService constructor.
     */
    public ComputerService() {
        computerDao = new ComputerDAOImpl();
    }

    @Override
    public List<Computer> get() {
        return computerDao.findAll();
    }

    @Override
    public Page<Computer> getPage() {
        List<Computer> l = computerDao.findAll();
        return new Page<Computer>(l, 1);
    }

    @Override
    public Page<Computer> getPage(int pageNumero, int length) {
        return new Page<Computer>(computerDao.findAll(pageNumero * length, length), pageNumero);
    }

    @Override
    public Computer get(int id) {
        return computerDao.getById(id);
    }

    @Override
    public boolean add(Computer computer) {
        return computerDao.add(computer);
    }

    @Override
    public boolean update(Computer computer) {
        return computerDao.update(computer);
    }

    @Override
    public boolean delete(int id) {
        return computerDao.delete(id);
    }

    @Override
    public Company getCompany(int id) {
        return computerDao.getCompany(id);
    }
}
