package com.excilys.computerdatabase.mappers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.daos.CompanyDAOImpl;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public class ComputerMapper {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerMapper.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Config.getProperties().getProperty("date_format"));

    private static String url;

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
            long idCompany              = rset.getLong(Computer.TABLE_NAME + Company.TABLE_NAME + Company.FIELD_ID);
            String nameCompany          = rset.getString(Computer.TABLE_NAME + Company.TABLE_NAME + Company.FIELD_NAME);

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

    /**
     * Create a computer from a DTO.
     * @param computerDTO the computerDTO
     * @return Computer
     */
    public static Computer createBean(ComputerDTO computerDTO) {
        Computer computer = null;

        CompanyDAOImpl cDAO = new CompanyDAOImpl();

        LocalDate introduced = (computerDTO.getIntroduced().equals("") ? null
                : LocalDate.parse(computerDTO.getIntroduced(), DATE_FORMATTER));
        LocalDate discontinued = (computerDTO.getDiscontinued().equals("") ? null
                : LocalDate.parse(computerDTO.getDiscontinued(), DATE_FORMATTER));

        computer = new Computer.Builder(computerDTO.getName()).id(computerDTO.getId()).introduced(introduced)
                .discontinued(discontinued).company(cDAO.getById(computerDTO.getCompany().getId())).build();

        return computer;
    }

    /**
     * Create a DTO from a computer.
     * @param computer the computer
     * @return ComputerDTO
     */
    public static ComputerDTO createDTO(Computer computer) {
        ComputerDTO computerDTO = new ComputerDTO();

        LocalDate introduced = computer.getIntroduced();
        LocalDate discontinued = computer.getDiscontinued();

        computerDTO.setId(computer.getId());
        computerDTO.setName(computer.getName());
        computerDTO.setIntroduced((introduced != null ? introduced.format(DATE_FORMATTER) : ""));
        computerDTO.setDiscontinued((discontinued != null ? discontinued.format(DATE_FORMATTER) : ""));
        computerDTO.setCompany(CompanyMapper.createDTO(computer.getCompany()));

        return computerDTO;
    }

    public static void setUrl(String url) {
        ComputerMapper.url = url;
    }

    public static String getUrl() {
        return url;
    }

}
