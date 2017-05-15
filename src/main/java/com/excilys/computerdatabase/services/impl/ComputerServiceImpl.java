package com.excilys.computerdatabase.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@Service("computerService")
@Transactional(readOnly = true)
public class ComputerServiceImpl implements ComputerService {

    @Autowired
    private ComputerDAO computerDAO;

    @Override
    public Page<ComputerDTO> getPage() {
        return getPage(1, count(null), null, null, null);
    }

    @Override
    public Page<ComputerDTO> getPage(int pageNumero, int length) {
        return getPage(pageNumero, length, null, "name", "ASC");
    }

    @Override
    public Page<ComputerDTO> getPage(int pageNumero, int length, String search, String column, String order) {
        return new Page<ComputerDTO>(
                computerDAO.findAll((pageNumero - 1) * length, length, search, column, order).stream()
                        .map(it -> ComputerMapper.createDTO(it)).collect(Collectors.toList()),
                pageNumero);
    }

    @Override
    public ComputerDTO getById(long id) throws ComputerNotFoundException {
        return ComputerMapper.createDTO(computerDAO.getById(id));
    }

    @Override
    public List<ComputerDTO> getByName(String name) {
        return computerDAO.getByName(name).stream().map(it -> ComputerMapper.createDTO(it))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ComputerDTO add(Computer computer) {
        return ComputerMapper.createDTO(computerDAO.add(computer));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ComputerDTO update(Computer computer) throws ComputerNotFoundException {

        return ComputerMapper.createDTO(computerDAO.update(computer));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void delete(long id) throws ComputerNotFoundException {
        computerDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = ComputerNotFoundException.class)
    public void delete(List<Long> ids) throws ComputerNotFoundException {
        computerDAO.delete(ids);
    }

    @Override
    public int count(String search) {
        return computerDAO.count(search);
    }

    @Override
    public CompanyDTO getCompany(long id) throws CompanyNotFoundException, ComputerNotFoundException {
        return CompanyMapper.createDTO(computerDAO.getCompany(id));
    }
}
