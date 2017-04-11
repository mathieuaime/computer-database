package daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import interfaces.ICompanyDAO;
import model.Company;
import model.Computer;

public class CompanyDAO extends DefaultDAO implements ICompanyDAO {
    
	//Search all the companies
    private final static String QUERY_FIND_COMPANIES 		= "SELECT * FROM " + Company.TABLE_NAME;    
    
    //Search one company by id 
    private final static String QUERY_FIND_COMPANY_BY_ID 	= "SELECT * FROM " + Company.TABLE_NAME 
    														+ " WHERE " + Company.FIELD_ID + " = ? ";
    //Add a company
    private final static String QUERY_ADD_COMPANY 			= "INSERT INTO " + Company.TABLE_NAME 
      														+ " (" + Company.FIELD_ID + ", " 
  															+ Company.FIELD_NAME + ") VALUES (?, ?)";
    
    //Delete a company
    private final static String QUERY_DELETE_COMPANY 		= "DELETE FROM " + Company.TABLE_NAME 
    														+ " WHERE " + Company.FIELD_ID + " = ? ";
    //Search the computers of a company
    private final static String QUERY_FIND_COMPUTERS 		= "SELECT " 
    														+ Computer.TABLE_NAME + "." + Computer.FIELD_ID 		+ ", " 
    														+ Computer.TABLE_NAME + "." + Computer.FIELD_NAME 		+ ", "
    														+ Computer.TABLE_NAME + "." + Computer.FIELD_INTRODUCED + ", "
															+ Computer.TABLE_NAME + "." + Computer.FIELD_DISCONTINUED
															
    														+ " FROM " + Computer.TABLE_NAME
    														
    														+ " INNER JOIN " + Company.TABLE_NAME 
    														+ " ON " + Computer.FIELD_COMPANY_ID + " = " 
    														+ Company.TABLE_NAME + "." + Company.FIELD_ID
    														
    														+ " WHERE " + Company.TABLE_NAME + "." 
    														+ Company.FIELD_ID + " =  ?";

    @Override
    /**
     * Returns all the companies in the db
     *
     * @return      the list of the companies 
     * @see         List<Company>
     */
	public List<Company> listCompanies() {
    	return listCompanies(-1, -1);
    }
    
    
	@Override
	//List the companies from offset to offset + length -1 
	/**
	 * Return a sub-list of the companies 
	 * From offset to offset + length -1
	 *
	 * @param  offset 	the start of the list
	 * @param  length 	the length of the list
	 * @return      	the list of the companies between offset & offset + length -1
	 * @see         	List<Company>
	 */
	public List<Company> listCompanies(int offset, int length) {
		List<Company> companies = new ArrayList<>();
		
		try {
            con 	= getConnexion();
            stmt 	= con.prepareStatement(QUERY_FIND_COMPANIES
            		//Si on a spécifié la taille, on le spécifie
            		+ (length != -1 ? " ORDER BY " + Company.FIELD_ID + " LIMIT " + length : "")
            		//Si on a spécifié l'offset, on le spécifie
            		+ (length != -1 && offset != -1 ? " OFFSET " + offset : ""));

            final ResultSet rset = stmt.executeQuery();

            //Parcours des résultats 
            while (rset.next()) {
                Company company = new Company(rset.getInt(Company.FIELD_ID), rset.getString(Company.FIELD_NAME));
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
	 * @param  id	the id of the company wanted
	 * @return      the company id
	 * @see         Company
	 */
	public Company getCompany(int id) {
		Company company = null;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_FIND_COMPANY_BY_ID);
            stmt.setInt(1, id);
            final ResultSet rset = stmt.executeQuery();

            if (rset.next()) {
                company = new Company(rset.getInt(Company.FIELD_ID), rset.getString(Company.FIELD_NAME));
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
	/**
	 * Add a new company in the db
	 *
	 * @param  id	the id of the new company
	 * @param  name the name of the new company
	 * @return      true if the company is added
	 * @see         boolean
	 */
	public boolean addCompany(int id, String name) {
		boolean add = false;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_ADD_COMPANY);
            stmt.setInt(1, id);
            stmt.setString(2, name);
            
            int res = stmt.executeUpdate();
            add = res == 1;

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
		
		return add;
	}

	@Override
	/**
	 * Update the company id in the db 
	 *
	 * @param  id	the id of the company
	 * @param  name the new name of the company
	 * @return      true if the company id updated
	 * @see         boolean
	 */
	public boolean updateCompany(int id, String name) {
		boolean add = false;
		
		try {
            con = getConnexion();
            StringBuilder sb = new StringBuilder("UPDATE " + Company.TABLE_NAME + " SET ");
            
        	sb.append(Company.FIELD_NAME + "= " + (name == null ? Company.FIELD_NAME : "'" + name + "'"));
            
            sb.append(" WHERE " + Company.FIELD_ID + " = " + id);
            
            stmt = con.prepareStatement(sb.toString());
            
            int res = stmt.executeUpdate();
            add = res == 1;

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
		
		return add;
	}

	@Override
	/**
	 * Delete the company id in the db 
	 *
	 * @param  id 	the id of the company
	 * @return      true if the company is deleted
	 * @see         boolean
	 */
	public boolean deleteCompany(int id) {
		boolean delete = false;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_DELETE_COMPANY);
            stmt.setInt(1, id);
            int res = stmt.executeUpdate();
            delete = res == 1;

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
		
		return delete;
	}
	

	@Override
	/**
	 * Return the list of the computers of the company id 
	 *
	 * @param  id 	the id of the company
	 * @return      the list of the computers
	 * @see         List<Computer>
	 */
	public List<Computer> getComputers(int id) {
		List<Computer> computers = new ArrayList<>();
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_FIND_COMPUTERS);
            stmt.setInt(1, id);

            final ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Computer computer = new Computer(rset.getInt(Computer.FIELD_ID), 
            								rset.getString(Computer.FIELD_NAME),
            								rset.getDate(Computer.FIELD_INTRODUCED),
            								rset.getDate(Computer.FIELD_DISCONTINUED),
            								id);
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
