package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;

public interface ComputerService {

    /**
     * Returns the list of the computers.
     * @return List<ComputerDTO>
     */
    List<ComputerDTO> get();

    /**
     * Returns the computer id.
     * @param id the id of the computer
     * @return ComputerDTO
     */
    ComputerDTO get(int id);

    /**
     * Add a computer.
     * @param computerDTO the computer to add
     * @return true if the computer is successfully added
     */
    boolean add(ComputerDTO computerDTO);

    /**
     * Update a computer.
     * @param computerDTO the computer to update
     * @return boolean true if the computer is successfully updated
     */
    boolean update(ComputerDTO computerDTO);

    /**
     * Delete a computer.
     * @param id the id of the computer
     * @return boolean true if the computer is successfully deleted
     */
    boolean delete(int id);

    /**
     * Return the number of computers.
     * @return int number of computers
     */
    int count();

    /**
     * Returns the company of the computer id.
     * @param id the id of the computer
     * @return Company
     */
    CompanyDTO getCompany(int id);
}
