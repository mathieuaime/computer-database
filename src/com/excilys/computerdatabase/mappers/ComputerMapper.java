package com.excilys.computerdatabase.mappers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerMapper {
	
	public static Computer getComputer(ResultSet rset) {
		
		Computer computer = null;
		
		try {
			long idComputer = rset.getLong(Computer.TABLE_NAME + Computer.FIELD_ID);
			String nameComputer = rset.getString(Computer.TABLE_NAME + Computer.FIELD_NAME);
			Date introducedComputer = rset.getDate(Computer.TABLE_NAME + Computer.FIELD_INTRODUCED);
			Date discontinuedComputer = rset.getDate(Computer.TABLE_NAME + Computer.FIELD_DISCONTINUED);
			long idCompany = rset.getLong(Company.TABLE_NAME + Company.FIELD_ID);
			String nameCompany = rset.getString(Company.TABLE_NAME + Company.FIELD_NAME);
			
			Company company = new Company.Builder(idCompany, nameCompany).build();
			
			computer = new Computer.Builder(nameComputer).id(idComputer).introduced((introducedComputer != null ? introducedComputer.toLocalDate() : null))
					.discontinued((discontinuedComputer != null ? discontinuedComputer.toLocalDate() : null)).company(company).build();
			
		} catch (SQLException | IntroducedAfterDiscontinuedException e) {
			e.printStackTrace();
		}
		
		return computer;
	}

}
