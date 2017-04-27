package com.excilys.computerdatabase.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.excilys.computerdatabase.daos.CompanyDAOImpl;
import com.excilys.computerdatabase.daos.ComputerDAOImpl;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.interfaces.CompanyService;
import com.excilys.computerdatabase.interfaces.PageServ;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;

public class CompanyServiceImpl implements CompanyService, PageServ<CompanyDTO> {

    private CompanyDAOImpl companyDAO;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyService.class);

    /**
     * CompanyService constructor.
     */
    public CompanyServiceImpl() {
        companyDAO = new CompanyDAOImpl();
    }

    @Override
    public List<CompanyDTO> get() {
        return companyDAO.findAll().stream().map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList());
        //return companyDAO.findAll().stream().parallel().map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public CompanyDTO getById(long id) {
        return CompanyMapper.createDTO(companyDAO.getById(id));
    }

    @Override
    public List<CompanyDTO> getByName(String name) {
        return companyDAO.getByName(name).stream().map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public Page<CompanyDTO> getPage() {
        return new Page<CompanyDTO>(get(), 1);
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length) {
        return getPage(pageNumero, length, null, "ASC", Company.FIELD_NAME);
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length, String search, String sort, String order) {
        return new Page<CompanyDTO>(companyDAO.findAll((pageNumero - 1) * length, length, order).stream()
                .map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList()), pageNumero);
    }

    @Override
    public List<ComputerDTO> getComputers(long id) {
        return companyDAO.getComputers(id).stream().map(it -> ComputerMapper.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public void delete(long id) throws CompanyNotFoundException {
        companyDAO.delete(id);
    }
}
