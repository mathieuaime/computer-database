package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.interfaces.ComputerDAO;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.mysql.jdbc.Statement;

public class ComputerDAOImpl implements ComputerDAO {

    private static final String QUERY_FIND_COMPUTER_1                   = "select computer.id AS computerid, computer.name AS computername, "
                                                                        + "computer.introduced AS computerintroduced, computer.discontinued AS computerdiscontinued, "
                                                                        + "company.id AS computercompanyid, company.name AS computercompanyname "
                                                                        + "FROM computer " 
                                                                        + "LEFT JOIN company ON computer.company_id=company.id ";
    
    private static final String QUERY_FIND_COMPUTER_2                   = "select computer.id AS computerid, computer.name AS computername, "
                                                                        + "computer.introduced AS computerintroduced, computer.discontinued AS computerdiscontinued, "
                                                                        + "company.id AS computercompanyid, company.name AS computercompanyname " 
                                                                        + "FROM computer "
                                                                        + "LEFT JOIN company ON computer.company_id=company.id";
    
    private static final String QUERY_FIND_COMPUTER_ORDER_BY_COMPANY    = "SELECT " + Computer.TABLE_NAME + "." + Computer.FIELD_ID + " AS "
                                                                        + Computer.TABLE_NAME + Computer.FIELD_ID + ", "
                                                                        + Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " AS "
                                                                        + Computer.TABLE_NAME + Computer.FIELD_NAME + ", "
                                                                        + Computer.TABLE_NAME + "." + Computer.FIELD_INTRODUCED + " AS "
                                                                        + Computer.TABLE_NAME + Computer.FIELD_INTRODUCED + ", "
                                                                        + Computer.TABLE_NAME + "." + Computer.FIELD_DISCONTINUED + " AS "
                                                                        + Computer.TABLE_NAME + Computer.FIELD_DISCONTINUED + ", "
                                                                        + Company.TABLE_NAME + "." + Company.FIELD_ID + " AS "
                                                                        + Computer.TABLE_NAME + Company.TABLE_NAME + Company.FIELD_ID + ", "
                                                                        + Company.TABLE_NAME + "." + Company.FIELD_NAME + " AS "
                                                                        + Computer.TABLE_NAME + Company.TABLE_NAME + Company.FIELD_NAME
                                                                        + " FROM " + Company.TABLE_NAME + " LEFT JOIN " + Computer.TABLE_NAME + " ON " + Computer.TABLE_NAME + "."
                                                                        + Computer.FIELD_COMPANY_ID + "=" + Company.TABLE_NAME + "." + Company.FIELD_ID;

    private static final String QUERY_FIND_COMPUTER_BY_ID               = QUERY_FIND_COMPUTER_1 + " WHERE " + Computer.TABLE_NAME + "."
                                                                        + Computer.FIELD_ID + " = ?";

    private static final String QUERY_FIND_COMPUTER_BY_NAME             = QUERY_FIND_COMPUTER_1 + " WHERE " + Computer.TABLE_NAME
                                                                        + "." + Computer.FIELD_NAME + " = ?";

    private static final String QUERY_ADD_COMPUTER                      = "INSERT INTO " + Computer.TABLE_NAME + " (" + Computer.FIELD_NAME + ", " + Computer.FIELD_INTRODUCED + ", " + Computer.FIELD_DISCONTINUED + ", "
                                                                        + Computer.FIELD_COMPANY_ID + ") VALUES(?, ?, ?, ?)";

    private static final String QUERY_UPDATE_COMPUTER                   = "UPDATE " + Computer.TABLE_NAME + " SET " + Computer.FIELD_NAME
                                                                        + " = ?, " + Computer.FIELD_INTRODUCED + " = ?, " + Computer.FIELD_DISCONTINUED + " = ?, "
                                                                        + Computer.FIELD_COMPANY_ID + " = ? " + " WHERE " + Computer.FIELD_ID + " = ?";

    private static final String QUERY_DELETE_COMPUTER                   = "DELETE FROM " + Computer.TABLE_NAME + " WHERE "
                                                                        + Computer.FIELD_ID + " IN (";
    
    private static final String QUERY_DELETE_COMPUTER_OF_COMPANY        = "DELETE FROM computer WHERE company_id =  ?";

    private static final String QUERY_FIND_COMPANY                      = "SELECT " + Company.TABLE_NAME + "." + Company.FIELD_ID + ", "
                                                                        + Company.TABLE_NAME + "." + Company.FIELD_NAME + " FROM " + Company.TABLE_NAME + " INNER JOIN "
                                                                        + Computer.TABLE_NAME + " ON " + Computer.FIELD_COMPANY_ID + " = " + Company.TABLE_NAME + "."
                                                                        + Company.FIELD_ID + " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_ID + " = ? ";

    private static final String QUERY_COUNT_COMPUTERS                   = "select count(*) as count from computer";
    
    private static final String QUERY_COUNT_COMPUTERS_SEARCH            = "SELECT (select count(*) from computer where name like ?) +" +
                                                                        "(select count(*) from computer inner join (select distinct(id) from company where name like ?) c on  computer.company_id = c.id) -" +
                                                                        "(select count(*) from computer inner join company on computer.company_id = company.id where computer.name like ? and company.name like ?) as count";
    
    private static int countTotal = -1;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerDAOImpl.class);

    @Override
    public List<Computer> findAll() {
        return findAll(0, count(null), null, "ASC", Computer.FIELD_ID);
    }

    @Override
    public List<Computer> findAll(int offset, int length, String search, String column, String order) {
        List<Computer> computers = new ArrayList<>();
        
        column = column == null ? "name" : column;
        order = order == null ? "ASC" : order;
        
        String query = "";
        boolean isSearch = search != null && !search.equals("");
        
        // recherche ordonnée par company
        if (column.startsWith("company")) {
            query = QUERY_FIND_COMPUTER_ORDER_BY_COMPANY
                    + (isSearch ? " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " LIKE '" + search
                    + "%' OR " + Company.TABLE_NAME + "." + Company.FIELD_NAME + " LIKE '" + search + "%'" : "")
                    + " ORDER BY " + Computer.TABLE_NAME + column + " " + order
                    + " LIMIT " + length + " OFFSET " + offset;
        } else {
            query = "(" + QUERY_FIND_COMPUTER_1;

            if (isSearch) {
                query += " WHERE computer.name LIKE '" + search + "%'";
            }

            query += " ORDER BY " + Computer.TABLE_NAME + column + " " + order;

            query += " LIMIT " + length + " OFFSET " + offset + ")";

            query += " union ";

            query += "(" + QUERY_FIND_COMPUTER_2;

            if (isSearch) {
                query += " WHERE company.name LIKE '" + search + "%'";
            }

            query += " ORDER BY " + Computer.TABLE_NAME + column + " " + order;

            query += " LIMIT " + length + " OFFSET " + offset + ")";

        }
        
        //LOGGER.debug(query);

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(query);) {

            con.setReadOnly(true);

            //long startTime = System.currentTimeMillis();
            final ResultSet rset = stmt.executeQuery();
            //long stopTime = System.currentTimeMillis();
            
            //LOGGER.debug((stopTime - startTime) + "ms : " + query);

            while (rset.next()) {
                computers.add(ComputerMapper.getComputer(rset));
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computers;
    }

    @Override
    public Computer getById(long id) {
        Computer computer = null;

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_ID);) {

            con.setReadOnly(true);

            stmt.setLong(1, id);
            final ResultSet rset = stmt.executeQuery();

            if (rset.next()) {

                computer = ComputerMapper.getComputer(rset);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computer;
    }

    @Override
    public List<Computer> getByName(String name) {
        List<Computer> computers = new ArrayList<>();

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_NAME);) {

            con.setReadOnly(true);
            stmt.setString(1, name);
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

    @Override
    public Computer add(Computer computer) {

        try (Connection con = ConnectionMySQL.getConnection();) {

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

        try (Connection con = ConnectionMySQL.getConnection();) {

            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            try (PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE_COMPUTER);) {

                con.setReadOnly(false);

                stmt.setString(1, computer.getName());
                stmt.setObject(2, computer.getIntroduced());
                stmt.setObject(3, computer.getDiscontinued());
                stmt.setLong(4, computer.getCompany().getId());
                stmt.setLong(5, computer.getId());
                
                //long startTime = System.currentTimeMillis();
                stmt.executeUpdate();
                //long stopTime = System.currentTimeMillis();
                //LOGGER.debug((stopTime - startTime) + " ms : " + stmt.toString());

                con.commit();

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
    }

    @Override
    public void delete(long id) throws ComputerNotFoundException {
        delete(new ArrayList<Long>(Arrays.asList(id)));
    }
    
    public void deleteFromCompany(long companyId) {
        
        try (Connection con = ConnectionMySQL.getConnection();) {

            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            try (PreparedStatement stmt = con.prepareStatement(QUERY_DELETE_COMPUTER_OF_COMPANY);) {

                con.setReadOnly(false);

                stmt.setLong(1, companyId);
                stmt.executeUpdate();

                con.commit();

                countTotal = -1;//a modifier

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Error: computers from company " + companyId + " not deleted -> " + e);
            } finally {
                con.setAutoCommit(oldAutoCommit);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    @Override
    public int count(String search) {
        //long startTime = System.currentTimeMillis();
        int count = 0;

        String query = QUERY_COUNT_COMPUTERS;

        if (search != null && !search.equals("")) {
            query = QUERY_COUNT_COMPUTERS_SEARCH;
            search += "%";
        }

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(query);) {

            con.setReadOnly(true);

            //long startTime2 = System.currentTimeMillis();

            boolean searchForTotalCount = search == null || search.equals("");

            if (searchForTotalCount && countTotal != -1) {
                count = countTotal;
            } else {

                if (!searchForTotalCount) {
                    stmt.setString(1, search);
                    stmt.setString(2, search);
                    stmt.setString(3, search);
                    stmt.setString(4, search);
                }

                final ResultSet rset = stmt.executeQuery();

                if (rset.next()) {
                    count = rset.getInt("count");
                    if (searchForTotalCount) {
                        countTotal = count;
                    }
                }
            }

            //long stopTime2 = System.currentTimeMillis();

            //LOGGER.debug((stopTime2 - startTime2) + "ms : " + query);


        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        //long stopTime = System.currentTimeMillis();
        //LOGGER.debug((stopTime - startTime) + "ms : Total " + query);
        //LOGGER.debug(count + "");
        return count;
    }

    @Override
    public Company getCompany(long id) {
        Company company = null;

        try (Connection con = ConnectionMySQL.getConnection();
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY);) {

            con.setReadOnly(true);
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
    public void delete(List<Long> listId) throws ComputerNotFoundException {
        String ids = "";

        try (Connection con = ConnectionMySQL.getConnection();) {

            con.setReadOnly(false);

            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            ids = listId.stream().map(Object::toString).collect(Collectors.joining(", "));

            String query = QUERY_DELETE_COMPUTER + ids + ")";

            try (PreparedStatement stmt = con.prepareStatement(query);) {

                //LOGGER.debug(stmt.toString());

                stmt.executeUpdate();

                con.commit();
                
                countTotal -= listId.size();

                //LOGGER.info("Info: Computer " + ids + " sucessfully deleted");

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Error: Computer " + ids + " not deleted");
            } finally {
                con.setAutoCommit(oldAutoCommit);
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

}
