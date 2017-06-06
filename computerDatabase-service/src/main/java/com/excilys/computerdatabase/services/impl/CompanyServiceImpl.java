package com.excilys.computerdatabase.services.impl;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.interfaces.CompanyDAO;
import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.impl.CompanyMapper;
import com.excilys.computerdatabase.mappers.impl.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;

@Service
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private ComputerDAO computerDAO;
    
    @Autowired
    CompanyMapper companyMapper;
    
    @Autowired
    ComputerMapper computerMapper;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Override
    public Page<Company> getPage() {
        LOGGER.info("getPage()");
        return getPage(new Page<>());
    }

    @Override
    public Page<Company> getPage(Page<?> page) {
        LOGGER.info("getPage(page: " + page + ")");
        return companyDAO.findAll(page);
    }

    @Override
    public Company getById(long id) throws CompanyNotFoundException {
        LOGGER.info("getById(id : " + id + ")");
        try {
            return companyDAO.getById(id);
        } catch (NotFoundException e) {
            throw new CompanyNotFoundException("Company " + id + "Not Found");
        }
    }

    @Override
    public List<Company> getByName(String name) {
        LOGGER.info("getByName(name : " + name + ")");
        return companyDAO.getByName(name);
    }

    @Override
    public List<Computer> getComputers(long id) throws CompanyNotFoundException {
        LOGGER.info("getComputers(id : " + id + ")");
        return companyDAO.getComputers(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { NotFoundException.class, ConstraintViolationException.class })
    public void delete(long id) throws CompanyNotFoundException {
        LOGGER.info("delete(id : " + id + ")");
        computerDAO.deleteFromCompany(id);
        companyDAO.delete(id);
    }

    @Override
    public Company save(Company object) throws NotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Company update(Company object) throws NotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(List<Long> ids) throws NotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(String search) {
        throw new UnsupportedOperationException();
    }
}
