package com.excilys.computerdatabase.daos.interfaces.template;

import java.util.List;

import com.excilys.computerdatabase.exceptions.NotFoundException;

public interface CrudDAO<T> {

    /**
     * Return the computer found by its id.
     * @param id the id of the computer
     * @return Computer
     * @throws ComputerNotFoundException when the computer doesn't exist
     * @throws CompanyNotFoundException when the company doesn't exist
     */
    T getById(long id) throws NotFoundException;

    /**
     * Returns the list of the computers found by its name.
     * @param name the name of the computer
     * @return List Computer
     */
    List<T> getByName(String name);

    /**
     * Add a computer.
     * @param computer the computer to add
     * @return Computer
     * @throws CompanyNotFoundException when the company doesn't exist
     */
    T save(T object) throws NotFoundException;

    /**
     * Update a object.
     * @param t the object to update
     * @return T
     */
    T update(T object) throws NotFoundException;

    /**
     * Delete a computer.
     * @param id the id of the computer
     */
    void delete(long id);

    /**
     * Delete a list of computer.
     * @param listId the list of ids of the computers
     */
    void delete(List<Long> listId);
}
