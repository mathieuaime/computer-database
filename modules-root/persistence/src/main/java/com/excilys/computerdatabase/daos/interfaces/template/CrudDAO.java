package com.excilys.computerdatabase.daos.interfaces.template;

import java.util.List;

import com.excilys.computerdatabase.exceptions.NotFoundException;

public interface CrudDAO<T> {

    /**
     * Return the computer found by its id.
     * @param id the id of the computer
     * @return Computer
     * @throws NotFoundException when the object doesn't exist
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
     * @param object the object to add
     * @return T
     * @throws NotFoundException when the company doesn't exist
     */
    T save(T object) throws NotFoundException;

    /**
     * Update a object.
     * @param object the object to update
     * @return T
     * @throws NotFoundException when the object is not found
     */
    T update(T object) throws NotFoundException;

    /**
     * Delete a object.
     * @param id the id of the object
     */
    void delete(long id);

    /**
     * Delete a list of objects.
     * @param listId the list of ids of the objects
     */
    void delete(List<Long> listId);
}
