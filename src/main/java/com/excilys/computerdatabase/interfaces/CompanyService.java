package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface CompanyService {

    /**
     * Returns the list of the companies.
     * @return List<Company>
     */
    List<Company> get();

    /**
     * Returns the company id.
     * @param id the id of the company
     * @return Company
     */
    Company get(int id);

    /**
     * Returns the list of the computers of the company id.
     * @param id the id of the company
     * @return List<Computer>
     */
    List<Computer> getComputers(int id);
}
