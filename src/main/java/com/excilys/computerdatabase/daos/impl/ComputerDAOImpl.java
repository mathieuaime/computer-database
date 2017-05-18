package com.excilys.computerdatabase.daos.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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

    private static final String QUERY_FIND_COMPUTER                     = "select c.id AS computerid, c.name AS computername, "
                                                                        + "c.introduced AS computerintroduced, c.discontinued AS computerdiscontinued, "
                                                                        + "co.id AS computercompanyid, co.name AS computercompanyname "
                                                                        + "FROM computer c "
                                                                        + "LEFT JOIN company co ON c.company_id=co.id ";

    private static final String QUERY_FIND_COMPUTER_ORDER_BY_COMPANY    = "SELECT c.id AS computerid,"
                                                                        + " c.name AS computername,"
                                                                        + " c.introduced AS computerintroduced,"
                                                                        + " c.discontinued AS computerdiscontinued,"
                                                                        + " co.id AS computercompanyid,"
                                                                        + " co.name AS computercompanyname"
                                                                        + " FROM (select * from company where id in (select distinct company_id from computer)) co LEFT JOIN computer c ON c.company_id = co.id";

    private static final String QUERY_FIND_COMPUTER_BY_ID               = QUERY_FIND_COMPUTER + " WHERE c.id = ?";

    private static final String QUERY_FIND_COMPUTER_BY_NAME             = QUERY_FIND_COMPUTER + " WHERE c.name = ?";

    private static final String QUERY_ADD_COMPUTER                      = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?)";

    private static final String QUERY_UPDATE                           = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

    private static final String QUERY_DELETE_COMPUTER                   = "DELETE FROM computer WHERE id IN (";

    private static final String QUERY_DELETE_COMPUTER_OF_COMPANY        = "DELETE FROM computer WHERE company_id =  ?";

    private static final String QUERY_FIND_COMPANY                      = "SELECT co.id, co.name FROM company co"
                                                                        + " RIGHT JOIN computer c ON company_id = co.id"
                                                                        + " WHERE c.id = ? ";

    private static final String QUERY_COUNT_COMPUTERS                   = "select count(id) as count from computer";

    private static final String QUERY_COUNT_COMPUTERS_SEARCH            = "SELECT (select count(id) from computer where name like ?) +" +
                                                                        "(select count(c.id) from computer c inner join (select distinct(id) from company where name like ?) co on c.company_id = co.id) -" +
                                                                        "(select count(c.id) from computer c inner join company co on c.company_id = co.id where c.name like ? and co.name like ?) as count";

    private static int countTotal = -1;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerDAOImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Computer> findAll() {
        LOGGER.info("findAll()");
        return findAll(0, 0, null, null, null);
    }

    @Override
    public List<Computer> findAll(int offset, int length, String search, String column, String order) {
        LOGGER.info("findAll(offset : " + offset + ", length : " + length + ", search : " + search + ", column : "
                + column + ", order : " + order + ")");
        column = column == null || column.equals("") ? "name" : column;
        order = order == null || order.equals("") ? "ASC" : order;
        length = length == 0 ? count(null) : length;

        String query = "";
        boolean isSearch = search != null && !search.equals("");

        // recherche ordonnée par company
        if (column.startsWith("company")) {
            LOGGER.debug("find order by company");
            query = QUERY_FIND_COMPUTER_ORDER_BY_COMPANY
                    + (isSearch ? " WHERE c.name LIKE '" + search + "%' OR co.name LIKE '" + search + "%'"
                            : "")
                    + " ORDER BY computer" + column + " " + order + " LIMIT " + length + " OFFSET " + offset;
        } else {
            query = "(" + QUERY_FIND_COMPUTER;

            if (isSearch) {
                query += " WHERE c.name LIKE '" + search + "%'";
            }

            query += " ORDER BY computer" + column + " " + order;
            query += " LIMIT " + length + " OFFSET " + offset + ")";

            if (isSearch) {
                LOGGER.debug("find with search");
                query += " union ";
                query += "(" + QUERY_FIND_COMPUTER;
                query += " WHERE co.name LIKE '" + search + "%'";
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
        LOGGER.info("getById(id : " + id + ")");
        try {
            return jdbcTemplate.queryForObject(QUERY_FIND_COMPUTER_BY_ID, new Object[] {id},
                    (rs, rowNum) -> ComputerMapper.getComputer(rs));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("ComputerNotFound");
            throw new ComputerNotFoundException("Computer " + id + " Not Found");
        }
    }

    @Override
    public List<Computer> getByName(String name) {
        LOGGER.info("getByName(name : " + name + ")");
        return jdbcTemplate.query(QUERY_FIND_COMPUTER_BY_NAME, new Object[] {name}, (rs, rowNum) -> {
            return ComputerMapper.getComputer(rs);
        });
    }

    @Override
    public Computer add(Computer computer) throws CompanyNotFoundException {
        LOGGER.info("add(computer : " + computer + ")");
        KeyHolder holder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(QUERY_ADD_COMPUTER, new Object[] { computer.getName(), computer.getIntroduced(),
                    computer.getDiscontinued(), computer.getCompany().getId() }, holder);
            ++countTotal;
            computer.setId(holder.getKey().longValue());
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("ComputerNotFound");
            throw new CompanyNotFoundException("Company " + computer.getCompany().getId() + " not found");
        }

        return computer;
    }

    @Override
    public Computer update(Computer computer) throws ComputerNotFoundException {
        LOGGER.info("update(computer : " + computer + ")");
        int res = jdbcTemplate.update(QUERY_UPDATE, new Object[] {computer.getName(), computer.getIntroduced(),
                computer.getDiscontinued(), computer.getCompany().getId(), computer.getId()});

        if (res == 0) {
            LOGGER.error("ComputerNotFound");
            throw new ComputerNotFoundException("Computer " + computer.getId() + " Not Found");
        }
        return computer;
    }

    @Override
    public void delete(long id) {
        LOGGER.info("delete(id : " + id + ")");
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
        LOGGER.info("count(search : " + search + ")");
        int count = 0;
        String query = QUERY_COUNT_COMPUTERS;
        boolean searchForTotalCount = search == null || search.equals("");

        if (searchForTotalCount && countTotal > 0) {
            count = countTotal;
        } else {

            if (!searchForTotalCount) {
                LOGGER.debug("count with search");
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
        LOGGER.info("getCompany(id : " + id + ")");
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
        LOGGER.info("delete(listId : " + listId + ")");
        String ids = listId.stream().map(Object::toString).collect(Collectors.joining(", "));
        countTotal -= jdbcTemplate.update(QUERY_DELETE_COMPUTER + ids + ")");
    }
}
