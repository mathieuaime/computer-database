package com.excilys.computerdatabase.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.excilys.computerdatabase.daos.ConnectionMySQL;
import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.mysql.jdbc.Statement;

public enum ComputerDAOImpl implements ComputerDAO {

    INSTANCE;

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
    
    private static final String QUERY_LOCK_FOR_UPDATE                   = "SELECT * FROM computer WHERE id = ? FOR UPDATE";

    private static final String QUERY_UPDATE_COMPUTER                   = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? " + " WHERE id = ?";

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

    @Override
    public List<Computer> findAll() {
        return findAll(0, 0, null, null, null);
    }

    @Override
    public List<Computer> findAll(int offset, int length, String search, String column, String order) {
        List<Computer> computers = new ArrayList<>();

        column = column == null ? "name" : column;
        order = order == null ? "ASC" : order;
        length = length == 0 ? count(null) : length;

        String query = "";
        boolean isSearch = search != null && !search.equals("");

        // recherche ordonnée par company
        if (column.startsWith("company")) {
            query = QUERY_FIND_COMPUTER_ORDER_BY_COMPANY
                    + (isSearch ? " WHERE computer.name LIKE '" + search
                    + "%' OR company.name LIKE '" + search + "%'" : "")
                    + " ORDER BY computer" + column + " " + order
                    + " LIMIT " + length + " OFFSET " + offset;
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

        LOGGER.debug(query);

        try {
            Connection con = ConnectionMySQL.getConnection();
            try (PreparedStatement stmt = con.prepareStatement(query);) {
                con.setReadOnly(true);
                computers = ComputerMapper.getComputers(stmt.executeQuery());
            }
        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computers;
    }

    @Override
    public Computer getById(long id) throws ComputerNotFoundException {
        Computer computer = null;

        try {
            Connection con = ConnectionMySQL.getConnection();

            try (PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_ID);) {
                con.setReadOnly(true);
                stmt.setLong(1, id);
                final ResultSet rset = stmt.executeQuery();

                if (rset.first()) {
                    computer = ComputerMapper.getComputer(rset);
                }
            }
        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        if (computer == null) {
            throw new ComputerNotFoundException("Computer Not Found");
        }

        return computer;
    }

    @Override
    public List<Computer> getByName(String name) {
        List<Computer> computers = new ArrayList<>();

        try {
            Connection con = ConnectionMySQL.getConnection();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_NAME);) {
                con.setReadOnly(true);
                stmt.setString(1, name);
                computers = ComputerMapper.getComputers(stmt.executeQuery());
            }
        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computers;
    }

    @Override
    public Computer add(Computer computer) {

        try {
            Connection con = ConnectionMySQL.getConnection();

            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            try (PreparedStatement stmt = con.prepareStatement(QUERY_ADD_COMPUTER, Statement.RETURN_GENERATED_KEYS);) {

                con.setReadOnly(false);

                stmt.setString(1, computer.getName());
                stmt.setObject(2, computer.getIntroduced());
                stmt.setObject(3, computer.getDiscontinued());
                stmt.setLong(4, computer.getCompany().getId());
                //long startTime = System.currentTimeMillis();
                stmt.executeUpdate();
                //long stopTime = System.currentTimeMillis();

                //LOGGER.debug((stopTime - startTime) + "ms : " + QUERY_ADD_COMPUTER);

                ResultSet resultSet = stmt.getGeneratedKeys();

                if (resultSet.first()) {
                    computer.setId(resultSet.getLong(1));
                }

                con.commit();

                ++countTotal;
                //LOGGER.info("Info: " + computer + " sucessfully added");

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Error: " + computer + " not added -> " + e);
            } finally {
                con.setAutoCommit(oldAutoCommit);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computer;
    }

    @Override
    public void update(Computer computer) throws ComputerNotFoundException {

        try {
            Connection con = ConnectionMySQL.getConnection();
            con.setAutoCommit(false);
            con.setReadOnly(false);

            try (PreparedStatement stmt = con.prepareStatement(QUERY_LOCK_FOR_UPDATE, ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_UPDATABLE);) {
                stmt.setLong(1, computer.getId());

                ResultSet rset = stmt.executeQuery();

                if (rset.first()) {

                    rset.updateString("name", computer.getName());
                    rset.updateObject("introduced", computer.getIntroduced());
                    rset.updateObject("discontinued", computer.getDiscontinued());
                    rset.updateLong("company_id", computer.getCompany().getId());

                    rset.updateRow();

                    con.commit();
                } else {
                    throw new ComputerNotFoundException("Computer not Found");
                }
            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Error: " + computer + " not updated -> " + e);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    @Override
    public void delete(long id) throws ComputerNotFoundException {
        delete(new ArrayList<Long>(Arrays.asList(id)));
    }


    @Override
    public void deleteFromCompany(long companyId) throws CompanyNotFoundException {

        try {
            Connection con = ConnectionMySQL.getConnection();

            con.setAutoCommit(false);

            try (PreparedStatement stmt = con.prepareStatement(QUERY_DELETE_COMPUTER_OF_COMPANY);) {

                con.setReadOnly(false);
                stmt.setLong(1, companyId);

                if (stmt.executeUpdate() == 0) {
                    throw new CompanyNotFoundException("Company Not Found");
                }

                countTotal = -1; // a modifier

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Error: computers from company " + companyId + " not deleted -> " + e);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    @Override
    public int count(String search) {
        // long startTime = System.currentTimeMillis();
        int count = 0;

        String query = QUERY_COUNT_COMPUTERS;

        if (search != null && !search.equals("")) {
            query = QUERY_COUNT_COMPUTERS_SEARCH;
            search += "%";
        }

        try {
            Connection con = ConnectionMySQL.getConnection();

            try (PreparedStatement stmt = con.prepareStatement(query);) {

                con.setReadOnly(true);

                // long startTime2 = System.currentTimeMillis();

                boolean searchForTotalCount = search == null || search.equals("");

                if (searchForTotalCount && countTotal > 0) {
                    count = countTotal;
                } else {

                    if (!searchForTotalCount) {
                        stmt.setString(1, search);
                        stmt.setString(2, search);
                        stmt.setString(3, search);
                        stmt.setString(4, search);
                    }

                    LOGGER.debug(stmt.toString());

                    final ResultSet rset = stmt.executeQuery();

                    if (rset.next()) {
                        count = rset.getInt("count");
                        if (searchForTotalCount) {
                            countTotal = count;
                        }
                    }
                }

                // long stopTime2 = System.currentTimeMillis();

                // LOGGER.debug((stopTime2 - startTime2) + "ms : " + query);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        // long stopTime = System.currentTimeMillis();
        // LOGGER.debug((stopTime - startTime) + "ms : Total " + query);
        // LOGGER.debug(count + "");
        return count;
    }

    @Override
    public Company getCompany(long id) throws CompanyNotFoundException, ComputerNotFoundException {
        Company company = null;

        try {
            Connection con = ConnectionMySQL.getConnection();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY);) {

                con.setReadOnly(true);
                stmt.setLong(1, id);
                final ResultSet rset = stmt.executeQuery();

                if (rset.first()) {
                    company = CompanyMapper.getCompany(rset);
                } else {
                    throw new ComputerNotFoundException("Computer Not Found");
                }
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        if (company == null || company.getName() == null) {
            throw new CompanyNotFoundException("Company Not Found");
        }

        return company;
    }

    @Override
    public void delete(List<Long> listId) throws ComputerNotFoundException {
        String ids = listId.stream().map(Object::toString).collect(Collectors.joining(", "));

        try {
            Connection con = ConnectionMySQL.getConnection();

            con.setReadOnly(false);
            con.setAutoCommit(false);

            String query = QUERY_DELETE_COMPUTER + ids + ")";

            try (PreparedStatement stmt = con.prepareStatement(query);) {

                if (stmt.executeUpdate() != listId.size()) {
                    throw new ComputerNotFoundException("Computer Not Found");
                }

                con.commit();

                countTotal -= listId.size();

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Error: Computer " + ids + " not deleted");
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

}
