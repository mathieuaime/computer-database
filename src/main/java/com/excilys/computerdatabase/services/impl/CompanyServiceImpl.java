package com.excilys.computerdatabase.services.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.ConnectionMySQL;
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
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private ComputerDAO computerDAO;

    //@Transactional
    @Override
    public CompanyDTO getById(long id) throws CompanyNotFoundException {
        ConnectionMySQL.open();
        try {
            return CompanyMapper.createDTO(companyDAO.getById(id));
        } finally {
            ConnectionMySQL.close();
        }
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
        return getPage(1, -1);
    }

    @Override
    public Page<CompanyDTO> getPage(int pageNumero, int length) {
        return getPage(pageNumero, length, null, "ASC", "name");
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
    public List<ComputerDTO> getComputers(long id) throws CompanyNotFoundException {
        ConnectionMySQL.open();
        try {
            return companyDAO.getComputers(id).stream().map(it -> ComputerMapper.createDTO(it))
                    .collect(Collectors.toList());
        } finally {
            ConnectionMySQL.close();
        }
    }

    @Override
    public void delete(long id) throws CompanyNotFoundException {
        ConnectionMySQL.open();
        try {
            computerDAO.deleteFromCompany(id);
            companyDAO.delete(id);
            ConnectionMySQL.getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMySQL.close();
        }
    }
}
