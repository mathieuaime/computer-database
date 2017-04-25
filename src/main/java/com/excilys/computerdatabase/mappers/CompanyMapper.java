package com.excilys.computerdatabase.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.models.Company;

public class CompanyMapper {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyMapper.class);

    /**
     * Create a company from a ResultSet.
     * @param rset the ResultSet
     * @return Company
     */
    public static Company getCompany(ResultSet rset) {

        Company company = null;

        try {
            int idCompany       = rset.getInt(Company.FIELD_ID);
            String nameCompany  = rset.getString(Company.FIELD_NAME);
            company = new Company.Builder(nameCompany).id(idCompany).build();
        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return company;
    }

    /**
     * Create a DTO from a company.
     * @param company the company
     * @return CompanyDTO
     */
    public static CompanyDTO createDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());

        return companyDTO;
    }

    /**
     * Create a company from a DTO.
     * @param companyDTO the companyDTO
     * @return Company
     */
    public static Company createBean(CompanyDTO companyDTO) {
        return new Company.Builder(companyDTO.getName()).id(companyDTO.getId()).build();
    }
}