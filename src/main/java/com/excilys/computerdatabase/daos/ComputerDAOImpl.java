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

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.interfaces.ComputerDAO;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerDAOImpl implements ComputerDAO {

    private static final String QUERY_FIND_COMPUTER             = "SELECT " + Computer.TABLE_NAME + "." + Computer.FIELD_ID + " AS "
                                                                + Computer.TABLE_NAME + Computer.FIELD_ID + "," + Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " AS "
                                                                + Computer.TABLE_NAME + Computer.FIELD_NAME + "," + Computer.TABLE_NAME + "." + Computer.FIELD_INTRODUCED
                                                                + " AS " + Computer.TABLE_NAME + Computer.FIELD_INTRODUCED + "," + Computer.TABLE_NAME + "."
                                                                + Computer.FIELD_DISCONTINUED + " AS " + Computer.TABLE_NAME + Computer.FIELD_DISCONTINUED + ","
                                                                + Company.TABLE_NAME + "." + Company.FIELD_ID + " AS " + Company.TABLE_NAME + Company.FIELD_ID + ","
                                                                + Company.TABLE_NAME + "." + Company.FIELD_NAME + " AS " + Company.TABLE_NAME + Company.FIELD_NAME
                                                                + " FROM " + Computer.TABLE_NAME + " LEFT JOIN " + Company.TABLE_NAME + " ON " + Computer.TABLE_NAME + "."
                                                                + Computer.FIELD_COMPANY_ID + "=" + Company.TABLE_NAME + "." + Company.FIELD_ID;

    private static final String QUERY_FIND_COMPUTER_BY_ID       = QUERY_FIND_COMPUTER + " WHERE " + Computer.TABLE_NAME + "."
                                                                + Computer.FIELD_ID + " = ?";

    private static final String QUERY_FIND_COMPUTER_BY_NAME     = QUERY_FIND_COMPUTER + " WHERE " + Computer.TABLE_NAME
                                                                + "." + Computer.FIELD_NAME + " = ?";

    private static final String QUERY_ADD_COMPUTER              = "INSERT INTO " + Computer.TABLE_NAME + " (" + Computer.FIELD_NAME + ", " + Computer.FIELD_INTRODUCED + ", " + Computer.FIELD_DISCONTINUED + ", "
                                                                + Computer.FIELD_COMPANY_ID + ") VALUES(?, ?, ?, ?)";

    private static final String QUERY_UPDATE_COMPUTER           = "UPDATE " + Computer.TABLE_NAME + " SET " + Computer.FIELD_NAME
                                                                + " = ?, " + Computer.FIELD_INTRODUCED + " = ?, " + Computer.FIELD_DISCONTINUED + " = ?, "
                                                                + Computer.FIELD_COMPANY_ID + " = ? " + " WHERE " + Computer.FIELD_ID + " = ?";

    private static final String QUERY_DELETE_COMPUTER           = "DELETE FROM " + Computer.TABLE_NAME + " WHERE "
                                                                + Computer.FIELD_ID + " IN (?) ";

    private static final String QUERY_FIND_COMPANY              = "SELECT " + Company.TABLE_NAME + "." + Company.FIELD_ID + ", "
                                                                + Company.TABLE_NAME + "." + Company.FIELD_NAME + " FROM " + Company.TABLE_NAME + " INNER JOIN "
                                                                + Computer.TABLE_NAME + " ON " + Computer.FIELD_COMPANY_ID + " = " + Company.TABLE_NAME + "."
                                                                + Company.FIELD_ID + " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_ID + " = ? ";

    private static final String QUERY_COUNT_COMPUTERS           = "SELECT COUNT(*) AS count " 
                                                                + " FROM " + Computer.TABLE_NAME 
                                                                + " LEFT JOIN " + Company.TABLE_NAME 
                                                                + " ON " + Computer.TABLE_NAME + "." + Computer.FIELD_COMPANY_ID + "=" + Company.TABLE_NAME + "." + Company.FIELD_ID;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerDAOImpl.class);

    private String url;

    /**
     * ComputerDAO constructor.
     */
    public ComputerDAOImpl() {
        this(Config.getProperties().getProperty("urlProd"));
    }

    /**
     * ComputerDAO constructor with a custom url.
     * @param url the url of the connexion.
     */
    public ComputerDAOImpl(String url) {
        this.url = url;
    }

    @Override
    public List<Computer> findAll() {
        return findAll(-1, -1, null, "ASC", Computer.FIELD_ID);
    }

    @Override
    public List<Computer> findAll(int offset, int length, String search, String sort, String order) {
        List<Computer> computers = new ArrayList<>();

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER
                        + (search != null ? " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " LIKE '%"
                                + search + "%' OR " + Company.TABLE_NAME + "." + Company.FIELD_NAME + " LIKE '%"
                                + search + "%'" : "")
                        + (length != -1 ? " ORDER BY "
                                + (sort != null && sort.equals("company") ? Company.TABLE_NAME : Computer.TABLE_NAME) + "."
                                + (sort != null ? (sort.equals("company") ? Company.FIELD_NAME : sort)
                                        : Computer.FIELD_NAME)
                                + " " + (order != null ? order : "ASC") + " LIMIT " + length : "")
                        + (length != -1 && offset != -1 ? " OFFSET " + offset : ""));) {

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
    public Computer getById(long id) {
        Computer computer = null;

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_ID);) {

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

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_NAME);) {
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
        boolean add = false;

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_ADD_COMPUTER);) {

            stmt.setString(1, computer.getName());
            stmt.setObject(2, computer.getIntroduced());
            stmt.setObject(3, computer.getDiscontinued());
            stmt.setLong(4, computer.getCompany().getId());
            int res = stmt.executeUpdate();
            add = res == 1;
            
            ResultSet resultSet = stmt.getGeneratedKeys();
            
            if(resultSet.first()) {
                computer.setId(resultSet.getLong(1));
            }

        } catch (SQLException e) {
            add = false;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        if (add) {
            LOGGER.info("Info: " + computer + " sucessfully added");
        } else {
            LOGGER.error("Error: " + computer + " not added");
        }
        
        return computer;
    }

    @Override
    public void update(Computer computer) throws ComputerNotFoundException {
        boolean update = false;

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE_COMPUTER);) {

            stmt.setString(1, computer.getName());
            stmt.setObject(2, computer.getIntroduced());
            stmt.setObject(3, computer.getDiscontinued());
            stmt.setLong(4, computer.getCompany().getId());
            stmt.setLong(5, computer.getId());
            int res = stmt.executeUpdate();
            update = res == 1;

        } catch (SQLException e) {
            update = false;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        if (update) {
            LOGGER.info("Info: " + computer + " sucessfully updated");
        } else {
            LOGGER.error("Error: " + computer + " not updated");
            throw new ComputerNotFoundException("Computer Not Found");
        }
    }

    @Override
    public void delete(long id) throws ComputerNotFoundException {
        delete(new ArrayList<Long>(Arrays.asList(id)));
    }

    @Override
    public int count(String search) {
        int count = 0;

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_COUNT_COMPUTERS + (search != null
                        ? " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " LIKE '%" + search + "%' OR "
                                + Company.TABLE_NAME + "." + Company.FIELD_NAME + " LIKE '%" + search + "%'"
                        : ""));) {
            final ResultSet rset = stmt.executeQuery();

            if (rset.next()) {
                count = rset.getInt("count");
            }

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return count;
    }

    @Override
    public Company getCompany(long id) {
        Company company = null;

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY);) {
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
        boolean delete = false;
        String ids = "";

        try (Connection con = ConnectionMySQL.INSTANCE.getConnection(url);
                PreparedStatement stmt = con.prepareStatement(QUERY_DELETE_COMPUTER);) {

            ids = listId.stream().map(Object::toString).collect(Collectors.joining(", "));

            stmt.setString(1, ids);
            int res = stmt.executeUpdate();
            delete = res == 1;

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        if (delete) {
            LOGGER.info("Info: Computer " + ids + " sucessfully deleted");
        } else {
            LOGGER.error("Error: Computer " + ids + " not deleted");
            throw new ComputerNotFoundException("Computer Not Found");
        }
    }

}
