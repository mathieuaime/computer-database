package com.excilys.computerdatabase.daos;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.interfaces.IComputerDAO;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerDAO extends DefaultDAO implements IComputerDAO {
    
    private final static String QUERY_FIND_COMPUTERS 		= "SELECT * FROM " + Computer.TABLE_NAME;
    
    private final static String QUERY_FIND_COMPUTER_BY_ID 	= "SELECT * FROM " 
														    + Computer.TABLE_NAME 
														    + " WHERE " + Computer.FIELD_ID + " = ? ";
    
    private final static String QUERY_ADD_COMPUTER 			= "INSERT INTO " 
														    + Computer.TABLE_NAME + " ("
														    + Computer.FIELD_ID + ", "
														    + Computer.FIELD_NAME + ", "
														    + Computer.FIELD_INTRODUCED + ", "
														    + Computer.FIELD_DISCONTINUED + ", "
														    + Computer.FIELD_COMPANY_ID 
														    + ") VALUES(?, ?, ?, ?, ?)";
    
    private final static String QUERY_DELETE_COMPUTER 		= "DELETE FROM " 
														    + Computer.TABLE_NAME 
														    + " WHERE " + Computer.FIELD_ID + " = ? ";
    
    private final static String QUERY_FIND_COMPANY 			= "SELECT "
    														+ Company.TABLE_NAME + "." + Company.FIELD_ID + ", "
    														+ Company.TABLE_NAME + "." + Company.FIELD_NAME
    														+ " FROM "
														    + Company.TABLE_NAME
														    + " INNER JOIN " + Computer.TABLE_NAME 
														    + " ON " + Computer.FIELD_COMPANY_ID + " = " 
														    + Company.TABLE_NAME + "." + Company.FIELD_ID
														    + " WHERE " + Computer.TABLE_NAME + "." 
														    + Computer.FIELD_ID + " = ? ";
    
	@Override
	public List<Computer> listComputers() {
		return listComputers(-1,-1);
	}
	
	@Override
	public List<Computer> listComputers(int offset, int length) {
		List<Computer> computers = new ArrayList<>();
		
		try {
            con 	= getConnexion();
            stmt 	= con.prepareStatement(QUERY_FIND_COMPUTERS 
            		+ (length != -1 ? " ORDER BY " + Computer.FIELD_ID + " LIMIT " + length : "") 
            		+ (length != -1 && offset != -1 ? " OFFSET " + offset : ""));
            
            final ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Computer computer = new Computer(rset.getInt(Computer.FIELD_ID), 
						                		rset.getString(Computer.FIELD_NAME), 
						                		rset.getDate(Computer.FIELD_INTRODUCED), 
						                		rset.getDate(Computer.FIELD_DISCONTINUED), 
						                		rset.getInt(Computer.FIELD_COMPANY_ID));
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

	@Override
	public Computer getComputer(int id) {
		Computer computer = null;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_FIND_COMPUTER_BY_ID);
            stmt.setInt(1, id);
            final ResultSet rset = stmt.executeQuery();

            if (rset.next()) {
                computer = new Computer(rset.getInt(Computer.FIELD_ID), 
				                		rset.getString(Computer.FIELD_NAME), 
				                		rset.getDate(Computer.FIELD_INTRODUCED), 
				                		rset.getDate(Computer.FIELD_DISCONTINUED), 
				                		rset.getInt(Computer.FIELD_COMPANY_ID));
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
		
		return computer;
	}

	@Override
	public boolean addComputer(int id, String name, Date introduced, Date discontinued, int companyId) {
		boolean add = false;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_ADD_COMPUTER);
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setObject(3, introduced);
            stmt.setObject(4, discontinued);
            stmt.setInt(5, companyId);
            int res = stmt.executeUpdate();
            add = res == 1;

        } catch (SQLException e) {
            add = false;
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
	public boolean updateComputer(int id, String name, Date introduced, Date discontinued, int companyId) {
		boolean update = false;
		
		try {
            con = getConnexion();
            StringBuilder sb = new StringBuilder();
        
        	sb.append("UPDATE " + Computer.TABLE_NAME + " SET ")
        	
        	.append(Computer.FIELD_NAME + " = ")
        	.append((name == null ? Computer.FIELD_NAME : "'" + name + "'") + ", ")
        	
        	.append(Computer.FIELD_INTRODUCED + " = ")
        	.append((introduced == null ? Computer.FIELD_INTRODUCED : "'" + introduced + "'") + ", ")
        	
        	.append(Computer.FIELD_DISCONTINUED	+ " = ")
        	.append((discontinued == null ? Computer.FIELD_DISCONTINUED : "'" + discontinued + "'") + ", ")

        	.append(Computer.FIELD_COMPANY_ID + " = ")
        	.append((companyId == 0 ? Computer.FIELD_COMPANY_ID : "'" + companyId + "'"))

            .append(" WHERE " + Computer.FIELD_ID + " = " + id);
            
            stmt = con.prepareStatement(sb.toString());
            
            int res = stmt.executeUpdate();
            
            update = res == 1;

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
		
		return update;
	}

	@Override
	public boolean deleteComputer(int id) {
		boolean delete = false;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_DELETE_COMPUTER);
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
	public Company getCompany(int id) {
		Company company = null;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_FIND_COMPANY);
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

}
