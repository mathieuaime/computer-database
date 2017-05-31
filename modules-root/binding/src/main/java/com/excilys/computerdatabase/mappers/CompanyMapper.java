package com.excilys.computerdatabase.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.mappers.interfaces.Mapper;
import com.excilys.computerdatabase.models.Company;

@Component
public class CompanyMapper implements Mapper<Company, CompanyDTO> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyMapper.class);

    @Override
    public Company bean(ResultSet rset) {
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

    @Override
    public List<Company> beans(ResultSet rset) {
        List<Company> companies = new ArrayList<>();

        try {
            while (rset.next()) {
                companies.add(bean(rset));
            }
        } catch (SQLException e) {
            LOGGER.debug("Exception: " + e);
        }

        return companies;
    }

    @Override
    public CompanyDTO dto(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        if (company != null) {
            companyDTO.setId(company.getId());
            companyDTO.setName(company.getName());
        }

        return companyDTO;
    }

    @Override
    public Company bean(CompanyDTO companyDTO) {
        return new Company.Builder(companyDTO.getName()).id(companyDTO.getId()).build();
    }
}