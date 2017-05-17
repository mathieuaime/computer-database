package com.excilys.computerdatabase.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mysql.jdbc.Statement;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

@Repository("computerDAO")
public class ComputerDAOImpl implements ComputerDAO {

    private static final String QUERY_FIND_COMPUTER                     = "select computer.id AS computerid, computer.name AS computername, "
                                                                        + "computer.introduced AS computerintroduced, computer.discontinued AS computerdiscontinued, "
                                                                        + "company.id AS computercompanyid, company.name AS computercompanyname "
                                                                        + "FROM computer "
                                                                        + "LEFT JOIN company ON computer.company_id=company.id ";

    private static final String QUERY_FIND_COMPUTER_ORDER_BY_COMPANY    = "SELECT computer.id AS computerid,"
                                                                        + " computer.name AS computername,"
                                                                        + " computer.introduced AS computerintroduced,"
                                                                        + " computer.discontinued AS computerdiscontinued,"
                                                                        + " company.id AS computercompanyid,"
                                                                        + " company.name AS computercompanyname"
                                                                        + " FROM company LEFT JOIN computer ON computer.company_id = company.id";

    private static final String QUERY_FIND_COMPUTER_BY_ID               = QUERY_FIND_COMPUTER + " WHERE computer.id = ?";

    private static final String QUERY_FIND_COMPUTER_BY_NAME             = QUERY_FIND_COMPUTER + " WHERE computer.name = ?";

    private static final String QUERY_ADD_COMPUTER                      = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?)";

    private static final String QUEREY_UPDATE                           = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

    private static final String QUERY_DELETE_COMPUTER                   = "DELETE FROM computer WHERE id IN (";

    private static final String QUERY_DELETE_COMPUTER_OF_COMPANY        = "DELETE FROM computer WHERE company_id =  ?";

    private static final String QUERY_FIND_COMPANY                      = "SELECT company.id, company.name FROM company"
                                                                        + " RIGHT JOIN computer ON company_id = company.id"
                                                                        + " WHERE computer.id = ? ";

    private static final String QUERY_COUNT_COMPUTERS                   = "select count(id) as count from computer";

    private static final String QUERY_COUNT_COMPUTERS_SEARCH            = "SELECT (select count(id) from computer where name like ?) +" +
                                                                        "(select count(computer.id) from computer inner join (select distinct(id) from company where name like ?) c on  computer.company_id = c.id) -" +
                                                                        "(select count(computer.id) from computer inner join company on computer.company_id = company.id where computer.name like ? and company.name like ?) as count";

    private static int countTotal = -1;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerDAOImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Computer> findAll() {
        return findAll(0, 0, null, null, null);
    }

    @Override
    public List<Computer> findAll(int offset, int length, String search, String column, String order) {
        column = column == null || column.equals("") ? "name" : column;
        order = order == null || order.equals("")? "ASC" : order;
        length = length == 0 ? count(null) : length;

        String query = "";
        boolean isSearch = search != null && !search.equals("");

        // recherche ordonnée par company
        if (column.startsWith("company")) {
            query = QUERY_FIND_COMPUTER_ORDER_BY_COMPANY
                    + (isSearch ? " WHERE computer.name LIKE '" + search + "%' OR company.name LIKE '" + search + "%'"
                            : "")
                    + " ORDER BY computer" + column + " " + order + " LIMIT " + length + " OFFSET " + offset;
        } else {
            query = "(" + QUERY_FIND_COMPUTER;

            if (isSearch) {
                query += " WHERE computer.name LIKE '" + search + "%'";
            }

            query += " ORDER BY computer" + column + " " + order;
            query += " LIMIT " + length + " OFFSET " + offset + ")";

            if (isSearch) {
                query += " union ";
                query += "(" + QUERY_FIND_COMPUTER;
                query += " WHERE company.name LIKE '" + search + "%'";
                query += " ORDER BY computer" + column + " " + order;
                query += " LIMIT " + length + " OFFSET " + offset + ")";
            }
        }

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            return ComputerMapper.getComputer(rs);
        });
    }

    @Override
    public Computer getById(long id) throws ComputerNotFoundException {
        try {
            return jdbcTemplate.queryForObject(QUERY_FIND_COMPUTER_BY_ID, new Object[] {id},
                    (rs, rowNum) -> ComputerMapper.getComputer(rs));
        } catch (EmptyResultDataAccessException e) {
            throw new ComputerNotFoundException("Computer " + id + " Not Found");
        }
    }

    @Override
    public List<Computer> getByName(String name) {
        return jdbcTemplate.query(QUERY_FIND_COMPUTER_BY_NAME, new Object[] {name}, (rs, rowNum) -> {
            return ComputerMapper.getComputer(rs);
        });
    }

    @Override
    public Computer add(Computer computer) throws CompanyNotFoundException {
        KeyHolder holder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(QUERY_ADD_COMPUTER,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, computer.getName());
                    ps.setObject(2, computer.getIntroduced());
                    ps.setObject(3, computer.getDiscontinued());
                    ps.setLong(4, computer.getCompany().getId());
                    return ps;
                }
            }, holder);

            ++countTotal;
            computer.setId(holder.getKey().longValue());
        } catch (DataIntegrityViolationException e) {
            throw new CompanyNotFoundException("Company " + computer.getCompany().getId() + " not found");
        } catch (DataAccessException e) {
            LOGGER.debug("error");
        }

        return computer;
    }

    @Override
    public Computer update(Computer computer) throws ComputerNotFoundException {
        int res = jdbcTemplate.update(QUEREY_UPDATE, new Object[] {computer.getName(), computer.getIntroduced(),
                computer.getDiscontinued(), computer.getCompany().getId(), computer.getId()});

        if (res == 0) {
            throw new ComputerNotFoundException("Computer " + computer.getId() + " Not Found");
        }
        return computer;
    }

    @Override
    public void delete(long id) {
        delete(new ArrayList<Long>(Arrays.asList(id)));
    }

    @Override
    public void deleteFromCompany(long companyId) throws CompanyNotFoundException {
        try {
            countTotal -= jdbcTemplate.update(QUERY_DELETE_COMPUTER_OF_COMPANY, new Object[] {companyId});
        } catch (DataAccessException e) {
            throw new CompanyNotFoundException("Company " + companyId + " Not Found");
        }
    }

    @Override
    public int count(String search) {
        int count = 0;
        String query = QUERY_COUNT_COMPUTERS;
        boolean searchForTotalCount = search == null || search.equals("");

        if (searchForTotalCount && countTotal > 0) {
            count = countTotal;
        } else {

            if (!searchForTotalCount) {
                query = QUERY_COUNT_COMPUTERS_SEARCH;
                search += "%";
            }

            count = jdbcTemplate.queryForObject(query,
                    (!searchForTotalCount ? new Object[] {search, search, search, search} : new Object[] {}),
                    (rs, rowNum) -> rs.getInt("count"));

            if (searchForTotalCount) {
                countTotal = count;
            }
        }

        return count;
    }

    @Override
    public Company getCompany(long id) throws CompanyNotFoundException, ComputerNotFoundException {
        try {
            Company company = jdbcTemplate.queryForObject(QUERY_FIND_COMPANY, new Object[] {id},
                    (rs, rowNum) -> CompanyMapper.getCompany(rs));

            if (company == null || company.getName() == null) {
                throw new CompanyNotFoundException("Company Not Found");
            }

            return company;
        } catch (EmptyResultDataAccessException e) {
            throw new ComputerNotFoundException("Computer " + id + " Not Found");
        }
    }

    @Override
    public void delete(List<Long> listId) {
        String ids = listId.stream().map(Object::toString).collect(Collectors.joining(", "));
        countTotal -= jdbcTemplate.update(QUERY_DELETE_COMPUTER + ids + ")");
    }
}
