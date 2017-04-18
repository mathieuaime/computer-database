package com.excilys.computerdatabase.services;

import java.util.List;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.daos.CompanyDAOImpl;
import com.excilys.computerdatabase.daos.ComputerDAOImpl;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.interfaces.CompanyService;
import com.excilys.computerdatabase.interfaces.PageServ;

public class CompanyServiceImpl implements CompanyService, PageServ<CompanyDTO> {

    private CompanyDAOImpl companyDAO;
    private ComputerDAOImpl computerDAO;

    /**
     * CompanyService constructor.
     */
    public CompanyServiceImpl() {
        companyDAO = new CompanyDAOImpl();
    }

    @Override
    public List<CompanyDTO> get() {
        return companyDAO.findAll().stream().map(it -> companyDAO.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public CompanyDTO get(int id) {
        return companyDAO.createDTO(companyDAO.getById(id));
    }

    @Override
    public Page<CompanyDTO> getPage() {
        return new Page<CompanyDTO>(get(), 1);
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length) {
        List<CompanyDTO> l = companyDAO.findAll(pageNumero * length, length).stream()
                .map(it -> companyDAO.createDTO(it)).collect(Collectors.toList());

        return new Page<CompanyDTO>(l, pageNumero);
    }

    @Override
    public List<ComputerDTO> getComputers(int id) {
        return companyDAO.getComputers(id).stream().map(it -> computerDAO.createDTO(it)).collect(Collectors.toList());
    }
}
