package com.excilys.computerdatabase.services.interfaces.template;

import java.util.List;

import org.springframework.security.access.annotation.Secured;

import com.excilys.computerdatabase.exceptions.NotFoundException;

public interface CrudService<T, U> {
    /**
     * Returns the computer of id id.
     * @param id the id of the computer
     * @return ComputerDTO
     * @throws NotFoundException exception when the computer is not found
     */
    T getById(long id) throws NotFoundException;

    /**
     * Returns the list of computers of name name.
     * @param name the name of the computer
     * @return List ComputerDTO
     */
    List<T> getByName(String name);

    /**
     * Add a computer.
     * @param computer the computer to add
     * @return T
     * @throws NotFoundException exception when the company is not found
     */
    //@Secured("ROLE_ADMIN")
    T save(U object) throws NotFoundException;

    /**
     * Update a computer.
     * @param computer the computer to update
     * @return T
     * @throws NotFoundException exception when the computer is not found
     */
    //@Secured("ROLE_ADMIN")
    T update(U object) throws NotFoundException;

    /**
     * Delete a computer.
     * @param id the id of the computer
     * @throws NotFoundException exception when the computer is not found
     */
    //@Secured("ROLE_ADMIN")
    void delete(long id) throws NotFoundException;

    /**
     * Delete a computer.
     * @param ids the ids of the computers
     * @throws NotFoundException exception when the computer is not found
     */
    //@Secured("ROLE_ADMIN")
    void delete(List<Long> ids) throws NotFoundException;

}
