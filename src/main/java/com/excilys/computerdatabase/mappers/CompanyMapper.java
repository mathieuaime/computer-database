package com.excilys.computerdatabase.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            company = new Company.Builder(rset.getString("name")).id(rset.getInt("id")).build();
        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }

        return company;
    }

    /**
     * Create a list of companies from a ResultSet.
     * @param rset the ResultSet
     * @return List Company
     */
    public static List<Company> getCompanies(ResultSet rset) {
        List<Company> companies = new ArrayList<>();

        try {
            while (rset.next()) {
                companies.add(getCompany(rset));
            }
        } catch (SQLException e) {
            LOGGER.debug("Exception: " + e);
        }

        return companies;
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