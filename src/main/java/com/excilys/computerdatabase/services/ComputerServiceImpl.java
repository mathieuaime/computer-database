package com.excilys.computerdatabase.services;

import java.util.List;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.daos.ComputerDAOImpl;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.interfaces.ComputerService;
import com.excilys.computerdatabase.interfaces.PageServ;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerServiceImpl implements ComputerService, PageServ<ComputerDTO> {

    private ComputerDAOImpl computerDao;

    /**
     * ComputerService constructor.
     */
    public ComputerServiceImpl() {
        computerDao = new ComputerDAOImpl();
    }

    @Override
    public List<Computer> get() {
        return computerDao.findAll();
    }

    @Override
    public Page<ComputerDTO> getPage() {

        List<ComputerDTO> l = computerDao.findAll().stream().map(it -> computerDao.createDTO(it))
                .collect(Collectors.toList());

        return new Page<ComputerDTO>(l, 1);
    }

    @Override
    public Page<ComputerDTO> getPage(int pageNumero, int length) {

        List<ComputerDTO> l = computerDao.findAll(pageNumero * length, length).stream()
                .map(it -> computerDao.createDTO(it)).collect(Collectors.toList());

        return new Page<ComputerDTO>(l, pageNumero);
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
    public int count() {
        return computerDao.count();
    }

    @Override
    public Company getCompany(int id) {
        return computerDao.getCompany(id);
    }
}
