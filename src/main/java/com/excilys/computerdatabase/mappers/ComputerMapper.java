package com.excilys.computerdatabase.mappers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerMapper {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerMapper.class);

    /**
     * Create a computer from a ResultSet.
     * @param rset the ResultSet
     * @return Computer
     */
    public static Computer getComputer(ResultSet rset) {

        Computer computer = null;

        try {
            long idComputer             = rset.getLong(Computer.TABLE_NAME + Computer.FIELD_ID);
            String nameComputer         = rset.getString(Computer.TABLE_NAME + Computer.FIELD_NAME);
            Date introducedComputer     = rset.getDate(Computer.TABLE_NAME + Computer.FIELD_INTRODUCED);
            Date discontinuedComputer   = rset.getDate(Computer.TABLE_NAME + Computer.FIELD_DISCONTINUED);
            long idCompany              = rset.getLong(Company.TABLE_NAME + Company.FIELD_ID);
            String nameCompany          = rset.getString(Company.TABLE_NAME + Company.FIELD_NAME);

            Company company = new Company.Builder(nameCompany).id(idCompany).build();

            computer = new Computer.Builder(nameComputer).id(idComputer)
                    .introduced((introducedComputer != null ? introducedComputer.toLocalDate() : null))
                    .discontinued((discontinuedComputer != null ? discontinuedComputer.toLocalDate() : null))
                    .company(company).build();

        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return computer;
    }

}
