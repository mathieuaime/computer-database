package com.excilys.computerdatabase.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.excilys.computerdatabase.interfaces.CompanyDAO;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class CompanyDAOImpl extends DefaultDAO implements CompanyDAO {

	// Search all the companies
	private final static String QUERY_FIND_COMPANIES = "SELECT * FROM " + Company.TABLE_NAME;

	// Search one company by id
	private final static String QUERY_FIND_COMPANY_BY_ID = "SELECT * FROM " + Company.TABLE_NAME + " WHERE "
			+ Company.FIELD_ID + " = ? ";

	// Search one company by name
	private final static String QUERY_FIND_COMPANY_BY_NAME = "SELECT * FROM " + Company.TABLE_NAME + " WHERE "
			+ Company.FIELD_NAME + " = ? ";

	// Add a company
	private final static String QUERY_ADD_COMPANY = "INSERT INTO " + Company.TABLE_NAME + " (" + Company.FIELD_ID + ", "
			+ Company.FIELD_NAME + ") VALUES (?, ?)";

	// Delete a company
	private final static String QUERY_DELETE_COMPANY = "DELETE FROM " + Company.TABLE_NAME + " WHERE "
			+ Company.FIELD_ID + " = ? ";
	// Search the computers of a company
	private final static String QUERY_FIND_COMPUTERS = "SELECT " + Computer.TABLE_NAME + "." + Computer.FIELD_ID + ", "
			+ Computer.TABLE_NAME + "." + Computer.FIELD_NAME + ", " + Computer.TABLE_NAME + "."
			+ Computer.FIELD_INTRODUCED + ", " + Computer.TABLE_NAME + "." + Computer.FIELD_DISCONTINUED

			+ " FROM " + Computer.TABLE_NAME

			+ " INNER JOIN " + Company.TABLE_NAME + " ON " + Computer.FIELD_COMPANY_ID + " = " + Company.TABLE_NAME
			+ "." + Company.FIELD_ID

			+ " WHERE " + Company.TABLE_NAME + "." + Company.FIELD_ID + " =  ?";

	@Override
	/**
	 * Returns all the companies in the db
	 *
	 * @return the list of the companies
	 * @see List<Company>
	 */
	public List<Company> findAll() {
		return findAll(-1, -1);
	}

	@Override
	// List the companies from offset to offset + length -1
	/**
	 * Return a sub-list of the companies From offset to offset + length -1
	 *
	 * @param offset
	 *            the start of the list
	 * @param length
	 *            the length of the list
	 * @return the list of the companies between offset & offset + length -1
	 * @see List<Company>
	 */
	public List<Company> findAll(int offset, int length) {
		List<Company> companies = new ArrayList<>();

		try {
			con = getConnexion();
			stmt = con.prepareStatement(QUERY_FIND_COMPANIES
					// Si on a spécifié la taille, on le spécifie
					+ (length != -1 ? " ORDER BY " + Company.FIELD_ID + " LIMIT " + length : "")
					// Si on a spécifié l'offset, on le spécifie
					+ (length != -1 && offset != -1 ? " OFFSET " + offset : ""));

			final ResultSet rset = stmt.executeQuery();

			// Parcours des résultats
			while (rset.next()) {
				Company company = new Company.Builder(rset.getInt(Company.FIELD_ID), rset.getString(Company.FIELD_NAME))
						.build();
				companies.add(company);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return companies;
	}

	@Override
	/**
	 * Return the company id in the db
	 *
	 * @param id
	 *            the id of the company wanted
	 * @return the company id
	 * @see Company
	 */
	public Company getById(int id) {
		Company company = null;

		try {
			con = getConnexion();
			stmt = con.prepareStatement(QUERY_FIND_COMPANY_BY_ID);
			stmt.setInt(1, id);
			final ResultSet rset = stmt.executeQuery();

			if (rset.next()) {
				int idCompany = rset.getInt(Company.FIELD_ID);
				String nameCompany = rset.getString(Company.FIELD_NAME);

				company = new Company.Builder(idCompany, nameCompany).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return company;
	}

	@Override
	public List<Company> getByName(String name) {
		List<Company> companies = new ArrayList<>();

		try {
			con = getConnexion();
			stmt = con.prepareStatement(QUERY_FIND_COMPANY_BY_NAME);
			stmt.setString(1, name);
			final ResultSet rset = stmt.executeQuery();

			if (rset.next()) {
				Company company = new Company.Builder(rset.getInt(Company.FIELD_ID), rset.getString(Company.FIELD_NAME))
						.build();
				companies.add(company);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return companies;
	}

	@Override
	/**
	 * Return the list of the computers of the company id
	 *
	 * @param id
	 *            the id of the company
	 * @return the list of the computers
	 * @see List<Computer>
	 */
	public List<Computer> getComputers(int id) {
		List<Computer> computers = new ArrayList<>();
		Company company = getById(id);

		try {
			con = getConnexion();
			stmt = con.prepareStatement(QUERY_FIND_COMPUTERS);
			stmt.setInt(1, id);

			final ResultSet rset = stmt.executeQuery();

			while (rset.next()) {

				long idComputer = rset.getLong(Computer.FIELD_ID);
				String nameComputer = rset.getString(Computer.FIELD_NAME);
				Date introducedComputer = rset.getDate(Computer.FIELD_INTRODUCED);
				Date discontinuedComputer = rset.getDate(Computer.FIELD_INTRODUCED);

				Computer computer = new Computer.Builder(idComputer, nameComputer).introduced(introducedComputer)
						.discontinued(discontinuedComputer).company(company).build();

				computers.add(computer);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return computers;
	}
}
