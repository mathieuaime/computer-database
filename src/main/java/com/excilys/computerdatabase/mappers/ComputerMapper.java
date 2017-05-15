package com.excilys.computerdatabase.mappers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;

public class ComputerMapper {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerMapper.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern(Config.getProperties().getProperty("date_format"));
    private static String url;
    private static CompanyService companyService;
    
    static {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext();
        context.scan("com.excilys.computerdatabase"); 
        context.refresh();
        
        companyService = (CompanyService) context.getBean("companyService");
        
        context.close();
    }

    /**
     * Create a computer from a ResultSet.
     * @param rset the ResultSet
     * @return Computer
     */
    public static Computer getComputer(ResultSet rset) {
        Computer computer = null;

        try {
            Date introducedComputer = rset.getDate("computerintroduced");
            Date discontinuedComputer = rset.getDate("computerdiscontinued");

            Company company = new Company.Builder(
                    rset.getString("computercompanyname"))
                            .id(rset.getLong("computercompanyid")).build();

            computer = new Computer.Builder(rset.getString("computername"))
                    .id(rset.getLong("computerid"))
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

    /**
     * Create a list of computers from a ResultSet.
     * @param rset the ResultSet
     * @return List Computer
     */
    public static List<Computer> getComputers(ResultSet rset) {
        List<Computer> computers = new ArrayList<>();

        try {
            while (rset.next()) {
                computers.add(getComputer(rset));
            }
        } catch (SQLException e) {
            LOGGER.debug("Exception " + e);
        }

        return computers;
    }

    /**
     * Create a computer from a DTO.
     * @param computerDTO the computerDTO
     * @return Computer
     * @throws CompanyNotFoundException Company Not Found
     */
    public static Computer createBean(ComputerDTO computerDTO) throws CompanyNotFoundException {
        LocalDate introduced = (computerDTO.getIntroduced().equals("") ? null
                : LocalDate.parse(computerDTO.getIntroduced(), DATE_FORMATTER));
        LocalDate discontinued = (computerDTO.getDiscontinued().equals("") ? null
                : LocalDate.parse(computerDTO.getDiscontinued(), DATE_FORMATTER));

        return new Computer.Builder(computerDTO.getName()).id(computerDTO.getId()).introduced(introduced)
                .discontinued(discontinued)
                .company(CompanyMapper.createBean(companyService.getById(computerDTO.getCompany().getId()))).build();
    }

    /**
     * Create a DTO from a computer.
     * @param computer the computer
     * @return ComputerDTO
     */
    public static ComputerDTO createDTO(Computer computer) {
        ComputerDTO computerDTO = new ComputerDTO();

        if (computer != null) {
            LocalDate introduced = computer.getIntroduced();
            LocalDate discontinued = computer.getDiscontinued();

            computerDTO.setId(computer.getId());
            computerDTO.setName(computer.getName());
            computerDTO.setIntroduced((introduced != null ? introduced.format(DATE_FORMATTER) : ""));
            computerDTO.setDiscontinued((discontinued != null ? discontinued.format(DATE_FORMATTER) : ""));
            computerDTO.setCompany(CompanyMapper.createDTO(computer.getCompany()));
        }

        return computerDTO;
    }

    public static void setUrl(String url) {
        ComputerMapper.url = url;
    }

    public static String getUrl() {
        return url;
    }
}
