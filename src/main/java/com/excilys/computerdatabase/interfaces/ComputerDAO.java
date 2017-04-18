package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.dtos.ComputerDTO;
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
     * @return List<Computer>
     */
    List<Computer> findAll(int offset, int length);

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
     * @return boolean true if the computer is successfully added
     */
    boolean add(Computer computer);

    /**
     * Update a computer.
     * @param computer the computer to update
     * @return boolean true if the computer is successfully updated
     */
    boolean update(Computer computer);

    /**
     * Delete a computer.
     * @param id the id of the computer
     * @return boolean true if the computer is successfully deleted
     */
    boolean delete(long id);

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
     */
    Computer createBean(ComputerDTO computerDTO);
}
