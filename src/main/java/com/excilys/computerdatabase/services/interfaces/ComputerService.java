package com.excilys.computerdatabase.services.interfaces;

import java.util.List;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.models.Computer;

public interface ComputerService extends PageService<ComputerDTO> {

    /**
     * Returns the computer of id id.
     * @param id the id of the computer
     * @return ComputerDTO
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    ComputerDTO getById(long id) throws ComputerNotFoundException;

    /**
     * Returns the list of computers of name name.
     * @param name the name of the computer
     * @return List ComputerDTO
     */
    List<ComputerDTO> getByName(String name);

    /**
     * Add a computer.
     * @param computer the computer to add
     * @return ComputerDTO
     */
    ComputerDTO add(Computer computer);

    /**
     * Update a computer.
     * @param computer the computer to update
     * @return ComputerDTO
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    ComputerDTO update(Computer computer) throws ComputerNotFoundException;

    /**
     * Delete a computer.
     * @param id the id of the computer
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    void delete(long id) throws ComputerNotFoundException;

    /**
     * Delete a computer.
     * @param ids the ids of the computers
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    void delete(List<Long> ids) throws ComputerNotFoundException;

    /**
     * Return the number of computers.
     * @return int number of computers
     * @param search the field to search for
     */
    int count(String search);

    /**
     * Returns the company of the computer id.
     * @param id the id of the computer
     * @return CompanyDTO
     * @throws CompanyNotFoundException Company Not Found Exception
     * @throws ComputerNotFoundException Computer Not Found Exception
     */
    CompanyDTO getCompany(long id) throws CompanyNotFoundException, ComputerNotFoundException;
}
