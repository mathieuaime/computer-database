package com.excilys.computerdatabase.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.excilys.computerdatabase.daos.ConnectionMySQL;
import com.excilys.computerdatabase.daos.interfaces.CompanyDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public enum CompanyDAOImpl implements CompanyDAO {
    
    INSTANCE;

    // Search all the companies
    private static final String QUERY_FIND_COMPANIES        = "SELECT * FROM company";

    // Search one company by id
    private static final String QUERY_FIND_COMPANY_BY_ID    = QUERY_FIND_COMPANIES + " WHERE id = ? ";

    // Search one company by name
    private static final String QUERY_FIND_COMPANY_BY_NAME  = QUERY_FIND_COMPANIES + " WHERE name = ? ";

    // Search the computers of a company
    private static final String QUERY_FIND_COMPUTERS        = "SELECT computer.id AS computerid,"
                                                            + " computer.name AS computername,"
                                                            + " computer.introduced AS computerintroduced,"
                                                            + " computer.discontinued AS computerdiscontinued,"
                                                            + " company.id AS computercompanyid,"
                                                            + " company.name AS computercompanyname"
                                                            + " FROM computer"
                                                            + " INNER JOIN company ON company_id = company.id"
                                                            + " WHERE company.id =  ?";

    private static final String QUERY_DELETE_COMPANY        = "DELETE FROM company WHERE id = ?";

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyDAOImpl.class);

    @Override
    public List<Company> findAll() {
        return findAll(0, -1, "name");
    }

    @Override
    public List<Company> findAll(int offset, int length, String order) {
        List<Company> companies = new ArrayList<>();

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANIES
                        + " ORDER BY " + order
                        + (length != -1 ? " LIMIT " + length + " OFFSET " + offset : ""));) {

            con.setReadOnly(true);
            companies = CompanyMapper.getCompanies(stmt.executeQuery());

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return companies;
    }

    @Override
    public Company getById(long id) {
        Company company = null;

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY_BY_ID);) {

            con.setReadOnly(true);
            stmt.setLong(1, id);
            final ResultSet rset = stmt.executeQuery();

            if (rset.first()) {
                company = CompanyMapper.getCompany(rset);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return company;
    }

    @Override
    public List<Company> getByName(String name) {
        List<Company> companies = new ArrayList<>();

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY_BY_NAME);) {

            con.setReadOnly(true);
            stmt.setString(1, name);

            companies = CompanyMapper.getCompanies(stmt.executeQuery());

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return companies;
    }

    @Override
    public List<Computer> getComputers(long id) {
        List<Computer> computers = new ArrayList<>();

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTERS);) {

            con.setReadOnly(true);
            stmt.setLong(1, id);

            computers = ComputerMapper.getComputers(stmt.executeQuery());

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computers;
    }

    @Override
    public void delete(long id) throws CompanyNotFoundException {

        Connection con;

        try {

            con = ConnectionMySQL.getConnection();

            con.setAutoCommit(false);

            try (PreparedStatement stmt = con.prepareStatement(QUERY_DELETE_COMPANY);) {

                con.setReadOnly(false);

                stmt.setLong(1, id);
                stmt.executeUpdate();

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Error: company " + id + " not deleted -> " + e);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

}
