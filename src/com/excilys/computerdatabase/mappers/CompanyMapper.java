package com.excilys.computerdatabase.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.computerdatabase.models.Company;

public class CompanyMapper {

		public static Company getCompany(ResultSet rset) {
			
			Company company = null;
			
			try {
				int idCompany = rset.getInt(Company.FIELD_ID);
				String nameCompany = rset.getString(Company.FIELD_NAME);
				company = new Company.Builder(idCompany, nameCompany).build();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return company;
		}
}
