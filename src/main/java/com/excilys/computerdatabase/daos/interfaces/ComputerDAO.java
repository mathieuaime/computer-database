package com.excilys.computerdatabase.daos.interfaces;

import java.util.List;

import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface ComputerDAO {
    /**
     * Returns the list of the computers.
     * @return List Computer
     */
    List<Computer> findAll();

    /**
     * Returns the list of the computers between offset and offset + length -1.
     * @param offset the first computer
     * @param length the number of computers
     * @param search the field to search for
     * @param sort the field to sort for
     * @param order the field to order by
     * @return List Computer
     */
    List<Computer> findAll(int offset, int length, String search, String sort, String order);

    /**
     * Return the computer found by its id.
     * @param id the id of the computer
     * @return Computer
     * @throws ComputerNotFoundException ComputerNotFoundException
     */
    Computer getById(long id) throws ComputerNotFoundException;

    /**
     * Returns the list of the computers found by its name.
     * @param name the name of the computer
     * @return List Computer
     */
    List<Computer> getByName(String name);

    /**
     * Add a computer.
     * @param computer the computer to add
     * @return Computer
     */
    Computer add(Computer computer);

    /**
     * Update a computer.
     * @param computer the computer to update
     * @return Computer
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    Computer update(Computer computer) throws ComputerNotFoundException;

    /**
     * Delete a computer.
     * @param id the id of the computer
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    void delete(long id) throws ComputerNotFoundException;

    /**
     * Delete a list of computer.
     * @param listId the list of ids of the computers
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    void delete(List<Long> listId) throws ComputerNotFoundException;

    /**
     * Return the number of computers.
     * @return int number of computers
     * @param search the field to search for
     */
    int count(String search);

    /**
     * Returns the Company of the computer id.
     * @param id the id of the computer
     * @return Company
     * @throws CompanyNotFoundException Company Not Found Exception
     * @throws ComputerNotFoundException Computer Not Found Exception
     */
    Company getCompany(long id) throws CompanyNotFoundException, ComputerNotFoundException;

    /**
     * Delete the computer of the company id.
     * @param companyId the company id
     * @throws CompanyNotFoundException Company Not Found
     */
    void deleteFromCompany(long companyId) throws CompanyNotFoundException;
}
