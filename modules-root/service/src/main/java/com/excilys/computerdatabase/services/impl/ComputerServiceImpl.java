package com.excilys.computerdatabase.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@Service
@Transactional(readOnly = true)
public class ComputerServiceImpl implements ComputerService {

    @Autowired
    private ComputerDAO computerDAO;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerServiceImpl.class);

    @Override
    public Page<ComputerDTO> getPage() {
        LOGGER.info("getPage()");
        return getPage(1, count(null), null, null, null);
    }

    @Override
    public Page<ComputerDTO> getPage(int pageNumero, int length) {
        LOGGER.info("getPage(pageNumero : " + pageNumero + ", length : " + length + ")");
        return getPage(pageNumero, length, null, "name", "ASC");
    }

    @Override
    public Page<ComputerDTO> getPage(Page<ComputerDTO> page) {
        LOGGER.info("getPage(page: " + page + ")");
        return getPage(page.getPage(), page.getPageSize(), page.getSearch(), page.getColumn(), page.getOrder());
    }

    @Override
    public Page<ComputerDTO> getPage(int pageNumero, int length, String search, String column, String order) {
        LOGGER.info("getPage(pageNumero : " + pageNumero + ", length : " + length + ", search : " + search
                + ", column : " + column + ", order : " + order + ")");
        return new Page<ComputerDTO>(computerDAO.findAll((pageNumero - 1) * length, length, search, column, order)
                .stream().map(it -> ComputerMapper.createDTO(it)).collect(Collectors.toList()), pageNumero, length);
    }

    @Override
    public ComputerDTO getById(long id) throws NotFoundException {
        LOGGER.info("getById(id : " + id + ")");
        return ComputerMapper.createDTO(computerDAO.getById(id));
    }

    @Override
    public List<ComputerDTO> getByName(String name) {
        LOGGER.info("getByName(name : " + name + ")");
        return computerDAO.getByName(name).stream().map(it -> ComputerMapper.createDTO(it))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = NotFoundException.class)
    public ComputerDTO save(Computer computer) throws NotFoundException {
        LOGGER.info("add(computer : " + computer + ")");
        return ComputerMapper.createDTO(computerDAO.save(computer));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = NotFoundException.class)
    public ComputerDTO update(Computer computer) throws NotFoundException {
        LOGGER.info("update(computer : " + computer + ")");
        return ComputerMapper.createDTO(computerDAO.update(computer));
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
    public CompanyDTO getCompany(long id) throws NotFoundException {
        LOGGER.info("getCompany(id : " + id + ")");
        return CompanyMapper.createDTO(computerDAO.getCompany(id));
    }
}
