package daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import interfaces.ComputerInterface;
import model.Computer;

public class ComputerDAO extends DefaultDAO implements ComputerInterface {
    
    private final static String QUERY_FIND_COMPUTER = "SELECT * FROM " + Computer.TABLE_NAME;
    private final static String QUERY_FIND_COMPUTER_BY_ID = "SELECT * FROM " + Computer.TABLE_NAME + " WHERE id = ? ";
    private final static String QUERY_ADD_COMPUTER = "INSERT INTO " + Computer.TABLE_NAME + " (id, name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?, ?)";
    private final static String QUERY_UPDATE_COMPUTER = "UPDATE " + Computer.TABLE_NAME + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
    private final static String QUERY_DELETE_COMPUTER = "DELETE FROM " + Computer.TABLE_NAME + " WHERE id = ? ";
    
	@Override
	public List<Computer> listComputers() {
		List<Computer> computers = new ArrayList<>();
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_FIND_COMPUTER);
            final ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Computer computer = new Computer(rset.getInt("id"), 
						                		rset.getString("name"), 
						                		rset.getTimestamp("introduced"), 
						                		rset.getTimestamp("discontinued"), 
						                		rset.getInt("company_id"));
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
            ResultSet rset = stmt.executeQuery();

            if (rset.next()) {
                computer = new Computer(rset.getInt("id"), 
				                		rset.getString("name"), 
				                		rset.getTimestamp("introduced"), 
				                		rset.getTimestamp("introduced"), 
				                		rset.getInt("company_id"));
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
	public boolean addComputer(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		boolean add = false;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_ADD_COMPUTER);
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setTimestamp(3, introduced);
            stmt.setTimestamp(4, discontinued);
            stmt.setInt(5, companyId);
            
            int res = stmt.executeUpdate();
            add = true;

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
	public boolean updateComputer(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		boolean update = false;
		
		try {
            con = getConnexion();
            stmt = con.prepareStatement(QUERY_UPDATE_COMPUTER);
            
            stmt.setString(1, name);
            stmt.setTimestamp(2, introduced);
            stmt.setTimestamp(3, discontinued);
            stmt.setInt(4, companyId);
            stmt.setInt(5, id);
            
            int res = stmt.executeUpdate();
            update = true;

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
            delete = true;

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

}
