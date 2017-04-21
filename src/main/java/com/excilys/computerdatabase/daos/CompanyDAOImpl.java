package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.interfaces.CompanyDAO;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class CompanyDAOImpl implements CompanyDAO {

    // Search all the companies
    private static final String QUERY_FIND_COMPANIES        = "SELECT * FROM " + Company.TABLE_NAME;

    // Search one company by id
    private static final String QUERY_FIND_COMPANY_BY_ID    = QUERY_FIND_COMPANIES + " WHERE " + Company.FIELD_ID + " = ? ";

    // Search one company by name
    private static final String QUERY_FIND_COMPANY_BY_NAME  = QUERY_FIND_COMPANIES + " WHERE " + Company.FIELD_NAME + " = ? ";

    // Search the computers of a company
    private static final String QUERY_FIND_COMPUTERS        = "SELECT "
                                                            + Computer.TABLE_NAME + "." + Computer.FIELD_ID + " AS " + Computer.TABLE_NAME + Computer.FIELD_ID + ", "
                                                            + Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " AS " + Computer.TABLE_NAME + Computer.FIELD_NAME + ", "
                                                            + Computer.TABLE_NAME + "." + Computer.FIELD_INTRODUCED + " AS " + Computer.TABLE_NAME + Computer.FIELD_INTRODUCED + ", "
                                                            + Computer.TABLE_NAME + "." + Computer.FIELD_DISCONTINUED + " AS " + Computer.TABLE_NAME + Computer.FIELD_DISCONTINUED + ", "
                                                            + Company.TABLE_NAME + "." + Company.FIELD_ID + " AS " + Company.TABLE_NAME + Company.FIELD_ID + ", "
                                                            + Company.TABLE_NAME + "." + Company.FIELD_NAME + " AS " + Company.TABLE_NAME + Company.FIELD_NAME
                                                            + " FROM " + Computer.TABLE_NAME
                                                            + " INNER JOIN " + Company.TABLE_NAME
                                                            + " ON " + Computer.FIELD_COMPANY_ID + " = " + Company.TABLE_NAME + "." + Company.FIELD_ID
                                                            + " WHERE " + Company.TABLE_NAME + "." + Company.FIELD_ID + " =  ?";

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyDAOImpl.class);

    private String url;

    /**
     * CompanyDAO constructor.
     */
    public CompanyDAOImpl() {
        this(Config.getProperties().getProperty("urlProd"));
    }

    /**
     * CompanyDAO constructor with a custom url.
     * @param url the url of the connexion.
     */
    public CompanyDAOImpl(String url) {
        this.url = url;
    }

    @Override
    public List<Company> findAll() {
        return findAll(-1, -1, Company.FIELD_ID);
    }

    @Override
    public List<Company> findAll(int offset, int length, String order) {
        List<Company> companies = new ArrayList<>();

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANIES
                        + (length != -1 ? " ORDER BY " + order + " LIMIT " + length : "")
                        + (length != -1 && offset != -1 ? " OFFSET " + offset : ""));) {

            final ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Company company = CompanyMapper.getCompany(rset);
                companies.add(company);
            }

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

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY_BY_ID);) {
            stmt.setLong(1, id);
            final ResultSet rset = stmt.executeQuery();

            if (rset.next()) {
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

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY_BY_NAME);) {
            stmt.setString(1, name);
            final ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Company company = CompanyMapper.getCompany(rset);
                companies.add(company);
            }

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

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTERS);) {
            stmt.setLong(1, id);

            final ResultSet rset = stmt.executeQuery();

            while (rset.next()) {

                Computer computer = ComputerMapper.getComputer(rset);

                computers.add(computer);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computers;
    }

}
