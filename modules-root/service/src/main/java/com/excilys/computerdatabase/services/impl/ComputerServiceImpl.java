package com.excilys.computerdatabase.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@Service
@Transactional(readOnly = true)
public class ComputerServiceImpl implements ComputerService {

    @Autowired
    private ComputerDAO computerDAO;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    ComputerMapper computerMapper;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerServiceImpl.class);

    @Override
    public Page<Computer> getPage() {
        LOGGER.info("getPage()");
        return getPage(new Page<>());
    }

    @Override
    public Page<Computer> getPage(Page<?> page) {
        LOGGER.info("getPage(page: " + page + ")");
        return computerDAO.findAll(page);
    }

    @Override
    public Computer getById(long id) throws NotFoundException {
        LOGGER.info("getById(id : " + id + ")");
        return computerDAO.getById(id);
    }

    @Override
    public List<Computer> getByName(String name) {
        LOGGER.info("getByName(name : " + name + ")");
        return computerDAO.getByName(name);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = NotFoundException.class)
    public Computer save(Computer computer) throws NotFoundException {
        LOGGER.info("add(computer : " + computer + ")");
        return computerDAO.save(computer);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = NotFoundException.class)
    public Computer update(Computer computer) throws NotFoundException {
        LOGGER.info("update(computer : " + computer + ")");
        return computerDAO.update(computer);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = NotFoundException.class)
    public void delete(long id) throws NotFoundException {
        LOGGER.info("delete(id : " + id + ")");
        computerDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = NotFoundException.class)
    public void delete(List<Long> ids) throws NotFoundException {
        LOGGER.info("delete(ids : " + ids + ")");
        computerDAO.delete(ids);
    }

    @Override
    public int count(String search) {
        LOGGER.info("count(search : " + search + ")");
        return computerDAO.count(search);
    }

    @Override
    public Company getCompany(long id) throws NotFoundException {
        LOGGER.info("getCompany(id : " + id + ")");
        return computerDAO.getCompany(id);
    }
}
