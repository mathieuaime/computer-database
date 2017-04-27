package com.excilys.computerdatabase.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.excilys.computerdatabase.daos.CompanyDAOImpl;
import com.excilys.computerdatabase.daos.ComputerDAOImpl;
import com.excilys.computerdatabase.daos.ConnectionMySQL;
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
    private ComputerDAOImpl computerDAO;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyService.class);

    /**
     * CompanyService constructor.
     */
    public CompanyServiceImpl() {
        companyDAO = new CompanyDAOImpl();
        computerDAO = new ComputerDAOImpl();
    }

    @Override
    public List<CompanyDTO> get() {
        ConnectionMySQL.open();
        List<CompanyDTO> l = companyDAO.findAll().stream().map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList());
        ConnectionMySQL.close();
        
        return l;
    }

    @Override
    public CompanyDTO getById(long id) {
        ConnectionMySQL.open();
        CompanyDTO c = CompanyMapper.createDTO(companyDAO.getById(id));
        ConnectionMySQL.close();
        
        return c;
    }

    @Override
    public List<CompanyDTO> getByName(String name) {
        ConnectionMySQL.open();
        List<CompanyDTO> l = companyDAO.getByName(name).stream().map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList());
        ConnectionMySQL.close();
        return l;
    }

    @Override
    public Page<CompanyDTO> getPage() {
        ConnectionMySQL.open();
        Page<CompanyDTO> p = new Page<CompanyDTO>(get(), 1);
        ConnectionMySQL.close();

        return p;
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length) {
        ConnectionMySQL.open();
        Page<CompanyDTO> p = getPage(pageNumero, length, null, "ASC", Company.FIELD_NAME);
        ConnectionMySQL.close();

        return p;
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length, String search, String sort, String order) {
        ConnectionMySQL.open();
        Page<CompanyDTO> p = new Page<CompanyDTO>(companyDAO.findAll((pageNumero - 1) * length, length, order).stream()
                .map(it -> CompanyMapper.createDTO(it)).collect(Collectors.toList()), pageNumero);
        ConnectionMySQL.close();
        return p;
    }

    @Override
    public List<ComputerDTO> getComputers(long id) {
        ConnectionMySQL.open();
        List<ComputerDTO> l = companyDAO.getComputers(id).stream().map(it -> ComputerMapper.createDTO(it))
                .collect(Collectors.toList());
        ConnectionMySQL.close();

        return l;
    }

    @Override
    public void delete(long id) throws CompanyNotFoundException {
        ConnectionMySQL.open();

        computerDAO.deleteFromCompany(id);

        companyDAO.delete(id);

        ConnectionMySQL.close();
    }
}
