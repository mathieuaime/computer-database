package com.excilys.computerdatabase.mappers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.mappers.interfaces.Mapper;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

@Component
public class ComputerMapper implements Mapper<Computer, ComputerDTO> {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerMapper.class);

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Computer bean(ResultSet rset) {
        Computer computer = new Computer("test");

        try {
            Date introducedComputer = rset.getDate("computerintroduced");
            Date discontinuedComputer = rset.getDate("computerdiscontinued");

            Company company = new Company.Builder(rset.getString("computercompanyname"))
                    .id(rset.getLong("computercompanyid")).build();

            computer = new Computer.Builder(rset.getString("computername")).id(rset.getLong("computerid"))
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

    @Override
    public List<Computer> beans(ResultSet rset) {
        List<Computer> computers = new ArrayList<>();

        try {
            while (rset.next()) {
                computers.add(bean(rset));
            }
        } catch (SQLException e) {
            LOGGER.debug("Exception " + e);
        }

        return computers;
    }

    @Override
    public Computer bean(ComputerDTO computerDTO) throws CompanyNotFoundException {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(computerDTO.getCompany().getId());
        companyDTO.setName(computerDTO.getCompany().getName());

        return new Computer.Builder(computerDTO.getName()).id(computerDTO.getId())
                .introduced(computerDTO.getIntroduced()).discontinued(computerDTO.getDiscontinued())
                .company(companyMapper.bean(companyDTO)).build();
    }

    @Override
    public ComputerDTO dto(Computer computer) {
        ComputerDTO computerDTO = new ComputerDTO();

        if (computer != null) {
            LocalDate introduced = computer.getIntroduced();
            LocalDate discontinued = computer.getDiscontinued();

            computerDTO.setId(computer.getId());
            computerDTO.setName(computer.getName());
            computerDTO.setIntroduced(introduced);
            computerDTO.setDiscontinued(discontinued);
            computerDTO.setCompany(companyMapper.dto(computer.getCompany()));
        }

        return computerDTO;
    }

}
