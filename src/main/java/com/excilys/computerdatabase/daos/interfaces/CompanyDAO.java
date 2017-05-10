package com.excilys.computerdatabase.daos.interfaces;

import java.util.List;

import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface CompanyDAO {

    /**
     * Returns the list of the companies.
     * @return List Company
     */
    List<Company> findAll();

    /**
     * Returns the list of the companies between offset and offset + length -1.
     * @param offset the first company
     * @param length the number of companies
     * @param order the field for order by
     * @return List Company
     */
    List<Company> findAll(int offset, int length, String order);

    /**
     * Return the company found by its id.
     * @param id id of the company
     * @return Company
     * @throws CompanyNotFoundException Company Not Found
     */
    Company getById(long id) throws CompanyNotFoundException;

    /**
     * Returns the list of the companies found by their names.
     * @param name name of the company
     * @return List Company
     */
    List<Company> getByName(String name);

    /**
     * Returns the computer of the company id.
     * @param id id of the company
     * @return List Computer
     * @throws CompanyNotFoundException Company Not Found
     */
    List<Computer> getComputers(long id) throws CompanyNotFoundException;

    /**
     * Delete a company and all its computers.
     * @param id the id of the company
     * @throws CompanyNotFoundException when the company does not exist
     */
    void delete(long id) throws CompanyNotFoundException;

}
