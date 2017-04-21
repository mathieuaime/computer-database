package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface CompanyDAO {

    /**
     * Returns the list of the companies.
     * @return List<Company>
     */
    List<Company> findAll();

    /**
     * Returns the list of the companies between offset and offset + length -1.
     * @param offset the first company
     * @param length the number of companies
     * @param order the field for order by
     * @return List<Company>
     */
    List<Company> findAll(int offset, int length, String order);

    /**
     * Return the company found by its id.
     * @param id id of the company
     * @return Company
     */
    Company getById(long id);

    /**
     * Returns the list of the companies found by their names.
     * @param name name of the company
     * @return List<Company>
     */
    List<Company> getByName(String name);

    /**
     * Returns the computer of the company id.
     * @param id id of the company
     * @return List<Computer>
     */
    List<Computer> getComputers(long id);

    /**
     * Create a DTO from a company.
     * @param company the company
     * @return CompanyDTO
     */
    //CompanyDTO createDTO(Company company);

    /**
     * Create a company from a DTO.
     * @param companyDTO the companyDTO
     * @return Company
     */
    //Company createBean(CompanyDTO companyDTO);
}
