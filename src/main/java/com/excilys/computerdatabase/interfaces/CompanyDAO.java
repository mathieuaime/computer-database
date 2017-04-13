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
     * @return List<Company>
     */
    List<Company> findAll(int offset, int length);

    /**
     * Return the company found by its id.
     * @param id id of the company
     * @return Company
     */
    Company getById(int id);

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
    List<Computer> getComputers(int id);
}
