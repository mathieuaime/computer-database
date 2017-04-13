package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.interfaces.ComputerDAO;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerDAOImpl implements ComputerDAO {

	private final static String QUERY_FIND_COMPUTER 		= "SELECT " 
															+ Computer.TABLE_NAME + "." + Computer.FIELD_ID + " AS " + Computer.TABLE_NAME + Computer.FIELD_ID + "," 
															+ Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " AS " + Computer.TABLE_NAME + Computer.FIELD_NAME + "," 
															+ Computer.TABLE_NAME + "." + Computer.FIELD_INTRODUCED + " AS " + Computer.TABLE_NAME + Computer.FIELD_INTRODUCED + "," 
															+ Computer.TABLE_NAME + "." + Computer.FIELD_DISCONTINUED + " AS " + Computer.TABLE_NAME + Computer.FIELD_DISCONTINUED + ","
															+ Company.TABLE_NAME + "." + Company.FIELD_ID + " AS " + Company.TABLE_NAME + Company.FIELD_ID + ","
															+ Company.TABLE_NAME + "." + Company.FIELD_NAME + " AS " + Company.TABLE_NAME + Company.FIELD_NAME
															+ " FROM " + Computer.TABLE_NAME + " LEFT JOIN " + Company.TABLE_NAME + " ON " + Computer.TABLE_NAME + "."
															+ Computer.FIELD_COMPANY_ID + "=" + Company.TABLE_NAME + "." + Company.FIELD_ID;
	

	private final static String QUERY_FIND_COMPUTER_BY_ID 	= QUERY_FIND_COMPUTER + " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_ID + " = ?";

	private final static String QUERY_FIND_COMPUTER_BY_NAME = QUERY_FIND_COMPUTER + " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_NAME + " = ?";

	private final static String QUERY_ADD_COMPUTER 			= "INSERT INTO " + Computer.TABLE_NAME 
															+ " (" + Computer.FIELD_ID + ", " 
															+ Computer.FIELD_NAME + ", " 
															+ Computer.FIELD_INTRODUCED + ", " 
															+ Computer.FIELD_DISCONTINUED + ", "
															+ Computer.FIELD_COMPANY_ID + ") VALUES(?, ?, ?, ?, ?)";
	
	private final static String QUERY_UPDATE_COMPUTER		= "UPDATE " + Computer.TABLE_NAME + " SET "
															+ Computer.FIELD_NAME + " = ?, "
															+ Computer.FIELD_INTRODUCED + " = ?, "
															+ Computer.FIELD_DISCONTINUED + " = ?, "
															+ Computer.FIELD_COMPANY_ID + " = ? "
															+ " WHERE " + Computer.FIELD_ID + " = ?";  

	private final static String QUERY_DELETE_COMPUTER 		= "DELETE FROM " + Computer.TABLE_NAME + " WHERE "
															+ Computer.FIELD_ID + " = ? ";

	private final static String QUERY_FIND_COMPANY 			= "SELECT " 
															+ Company.TABLE_NAME + "." + Company.FIELD_ID + ", "
															+ Company.TABLE_NAME + "." + Company.FIELD_NAME 
															+ " FROM " + Company.TABLE_NAME 
															+ " INNER JOIN " + Computer.TABLE_NAME 
															+ " ON " + Computer.FIELD_COMPANY_ID + " = " + Company.TABLE_NAME + "." + Company.FIELD_ID 
															+ " WHERE " + Computer.TABLE_NAME + "." + Computer.FIELD_ID + " = ? ";

	@Override
	public List<Computer> findAll() {
		return findAll(-1, -1);
	}

	@Override
	public List<Computer> findAll(int offset, int length) {
		List<Computer> computers = new ArrayList<>();

		try (Connection con = ConnectionMySQL.INSTANCE.getConnection();
				PreparedStatement stmt = con
						.prepareStatement(QUERY_FIND_COMPUTER
								+ (length != -1 ? " ORDER BY " + Computer.TABLE_NAME + "." + Computer.FIELD_ID
										+ " LIMIT " + length : "")
								+ (length != -1 && offset != -1 ? " OFFSET " + offset : ""));) {

			final ResultSet rset = stmt.executeQuery();

			while (rset.next()) {

				Computer computer = ComputerMapper.getComputer(rset);

				computers.add(computer);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return computers;
	}

	@Override
	public Computer getById(int id) {
		Computer computer = null;

		try (Connection con = ConnectionMySQL.INSTANCE.getConnection();
				PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_ID);) {

			stmt.setInt(1, id);
			final ResultSet rset = stmt.executeQuery();

			if (rset.next()) {

				computer = ComputerMapper.getComputer(rset);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return computer;
	}

	@Override
	public List<Computer> getByName(String name) {
		List<Computer> computers = new ArrayList<>();

		try (Connection con = ConnectionMySQL.INSTANCE.getConnection();
				PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_NAME);
				){
			stmt.setString(1, name);
			final ResultSet rset = stmt.executeQuery();

			if (rset.next()) {

				Computer computer = ComputerMapper.getComputer(rset);

				computers.add(computer);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return computers;
	}

	@Override
	public boolean add(Computer computer) {
		boolean add = false;

		try (Connection con = ConnectionMySQL.INSTANCE.getConnection();
				PreparedStatement stmt = con.prepareStatement(QUERY_ADD_COMPUTER);) {

			stmt.setLong(1, computer.getId());
			stmt.setString(2, computer.getName());
			stmt.setObject(3, computer.getIntroduced());
			stmt.setObject(4, computer.getDiscontinued());
			stmt.setLong(5, computer.getCompany().getId());
			int res = stmt.executeUpdate();
			add = res == 1;

		} catch (SQLException e) {
			add = false;
		}

		return add;
	}

	@Override
	public boolean update(Computer computer) {
		boolean update = false;

		try (Connection con = ConnectionMySQL.INSTANCE.getConnection();
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
		}

		return update;
	}

	@Override
	public boolean delete(int id) {
		boolean delete = false;

		try (Connection con = ConnectionMySQL.INSTANCE.getConnection();
				PreparedStatement stmt = con.prepareStatement(QUERY_DELETE_COMPUTER);) {

			stmt.setInt(1, id);
			int res = stmt.executeUpdate();
			delete = res == 1;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return delete;
	}

	@Override
	public Company getCompany(int id) {
		Company company = null;

		try (Connection con = ConnectionMySQL.INSTANCE.getConnection();
				PreparedStatement stmt = con.prepareStatement(QUERY_FIND_COMPANY);) {
			stmt.setInt(1, id);
			final ResultSet rset = stmt.executeQuery();

			if (rset.next()) {
				company = CompanyMapper.getCompany(rset);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return company;
	}

}
