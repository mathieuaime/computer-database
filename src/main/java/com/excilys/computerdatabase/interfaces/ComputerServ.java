package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface ComputerServ {

    /**
     * Returns the list of the computers.
     * @return List<Computer>
     */
    List<Computer> get();

    /**
     * Returns the computer id.
     * @param id the id of the computer
     * @return Computer
     */
    Computer get(int id);

    /**
     * Add a computer.
     * @param computer the computer to add
     * @return true if the computer is successfully added
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
    boolean delete(int id);

    /**
     * Returns the company of the computer id.
     * @param id the id of the computer
     * @return Company
     */
    Company getCompany(int id);
}
