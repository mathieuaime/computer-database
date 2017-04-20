package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface ComputerDAO {

    /**
     * Returns the list of the computers.
     * @return List<Computer>
     */
    List<Computer> findAll();

    /**
     * Returns the list of the computers between offset and offset + length -1.
     * @param offset the first computer
     * @param length the number of computers
     * @param order the field for order by
     * @return List<Computer>
     */
    List<Computer> findAll(int offset, int length, String order);

    /**
     * Return the computer found by its id.
     * @param id the id of the computer
     * @return Computer
     */
    Computer getById(long id);

    /**
     * Returns the list of the computers found by its name.
     * @param name the name of the computer
     * @return List<Computer>
     */
    List<Computer> getByName(String name);

    /**
     * Add a computer.
     * @param computer the computer to add
     */
    void add(Computer computer);

    /**
     * Update a computer.
     * @param computer the computer to update
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    void update(Computer computer) throws ComputerNotFoundException;

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
     */
    int count();

    /**
     * Returns the Company of the computer id.
     * @param id the id of the computer
     * @return Company
     */
    Company getCompany(long id);

    /**
     * Create a DTO from a computer.
     * @param computer the computer
     * @return ComputerDTO
     */
    ComputerDTO createDTO(Computer computer);

    /**
     * Create a computer from a DTO.
     * @param computerDTO the computerDTO
     * @return Computer
     * @throws IntroducedAfterDiscontinuedException exception when the introduced date is before the dicontinued date
     * @throws NameEmptyException exception when the name is empty
     */
    Computer createBean(ComputerDTO computerDTO) throws IntroducedAfterDiscontinuedException, NameEmptyException;
}
