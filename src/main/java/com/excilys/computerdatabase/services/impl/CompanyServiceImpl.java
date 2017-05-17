package com.excilys.computerdatabase.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.interfaces.CompanyDAO;
import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.services.interfaces.CompanyService;

@Service("companyService")
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private ComputerDAO computerDAO;

    @Override
    public CompanyDTO getById(long id) throws CompanyNotFoundException {
        return CompanyMapper.createDTO(companyDAO.getById(id));
    }

    @Override
    public List<CompanyDTO> getByName(String name) {
        return companyDAO.getByName(name).stream().map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public Page<CompanyDTO> getPage() {
        return getPage(1, -1);
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length) {
        return getPage(pageNumero, length, null, "ASC", "name");
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length, String search, String sort, String order) {
        return new Page<CompanyDTO>(companyDAO.findAll((pageNumero - 1) * length, length, order).stream()
                .map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList()), pageNumero);
    }

    @Override
    public List<ComputerDTO> getComputers(long id) throws CompanyNotFoundException {
        return companyDAO.getComputers(id).stream().map(it -> ComputerMapper.createDTO(it))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = CompanyNotFoundException.class)
    public void delete(long id) throws CompanyNotFoundException {
        computerDAO.deleteFromCompany(id);
        companyDAO.delete(id);
    }
}
