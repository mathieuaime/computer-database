package com.excilys.computerdatabase.daos.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.daos.interfaces.CompanyDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

@Repository("companyDAO")
public class CompanyDAOImpl implements CompanyDAO {

    private static final String QUERY_FIND_COMPANIES        = "SELECT * FROM company";

    private static final String QUERY_FIND_COMPANY_BY_ID    = QUERY_FIND_COMPANIES + " WHERE id = ? ";

    private static final String QUERY_FIND_COMPANY_BY_NAME  = QUERY_FIND_COMPANIES + " WHERE name = ? ";

    private static final String QUERY_FIND_COMPUTERS        = "SELECT computer.id AS computerid,"
                                                            + " computer.name AS computername,"
                                                            + " computer.introduced AS computerintroduced,"
                                                            + " computer.discontinued AS computerdiscontinued,"
                                                            + " company.id AS computercompanyid,"
                                                            + " company.name AS computercompanyname"
                                                            + " FROM computer"
                                                            + " LEFT JOIN company ON company_id = company.id"
                                                            + " WHERE company.id =  ?";

    private static final String QUERY_DELETE_COMPANY        = "DELETE FROM company WHERE id = ?";

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyDAOImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Company> findAll() {
        return findAll(0, -1, "name");
    }

    @Override
    public List<Company> findAll(int offset, int length, String order) {
        String query = QUERY_FIND_COMPANIES + " ORDER BY " + order
                + (length != -1 ? " LIMIT " + length + " OFFSET " + offset : "");

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            return CompanyMapper.getCompany(rs);
        });
    }

    @Override
    public Company getById(long id) throws CompanyNotFoundException {
        try {
            return jdbcTemplate.queryForObject(QUERY_FIND_COMPANY_BY_ID, new Object[] {id},
                    (rs, rowNum) -> CompanyMapper.getCompany(rs));
        } catch (EmptyResultDataAccessException e) {
            throw new CompanyNotFoundException("Company " + id + " Not Found");
        }
    }

    @Override
    public List<Company> getByName(String name) {
        return jdbcTemplate.query(QUERY_FIND_COMPANY_BY_NAME, new Object[] {name}, (rs, rowNum) -> {
            return CompanyMapper.getCompany(rs);
        });
    }

    @Override
    public List<Computer> getComputers(long id) throws CompanyNotFoundException {
        return jdbcTemplate.query(QUERY_FIND_COMPUTERS, new Object[] {id}, (rs, rowNum) -> {
            return ComputerMapper.getComputer(rs);
        });
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(QUERY_DELETE_COMPANY, new Object[] {id});
    }
}
