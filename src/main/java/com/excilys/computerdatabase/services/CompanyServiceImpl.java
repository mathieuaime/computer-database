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
import com.excilys.computerdatabase.models.Company;

public class CompanyServiceImpl implements CompanyService, PageServ<CompanyDTO> {

    private CompanyDAOImpl companyDAO;
    private ComputerDAOImpl computerDAO;

    /**
     * CompanyService constructor.
     */
    public CompanyServiceImpl() {
        companyDAO = new CompanyDAOImpl();
    }

    /**
     * CompanyService constructor with a custom url.
     * @param url the url of the connexion.
     */
    public CompanyServiceImpl(String url) {
        companyDAO = new CompanyDAOImpl(url);
    }

    @Override
    public List<CompanyDTO> get() {
        return companyDAO.findAll().stream().map(it -> companyDAO.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public CompanyDTO getById(long id) {
        return companyDAO.createDTO(companyDAO.getById(id));
    }

    @Override
    public List<CompanyDTO> getByName(String name) {
        return companyDAO.getByName(name).stream().map(it -> companyDAO.createDTO(it)).collect(Collectors.toList());
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
        List<CompanyDTO> l = companyDAO.findAll((pageNumero - 1) * length, length, order).stream()
                .map(it -> companyDAO.createDTO(it)).collect(Collectors.toList());

        return new Page<CompanyDTO>(l, pageNumero);
    }

    @Override
    public List<ComputerDTO> getComputers(long id) {
        return companyDAO.getComputers(id).stream().map(it -> computerDAO.createDTO(it)).collect(Collectors.toList());
    }
}
