package com.excilys.computerdatabase.daos.interfaces.template;

import com.excilys.computerdatabase.models.Page;

public interface PageDAO<T> {
    
    /**
     * Returns the list of the computers.
     * @return List Computer
     */
    Page<T> findAll();

    /**
     * Returns the list of the computers between offset and offset + length -1.
     * @param page the page
     * @return Page T
     */
    Page<T> findAll(Page<?> page);
    
    /**
     * Return the number of computers.
     * @return int number of computers
     * @param search the field to search for
     */
    int count(String search);
}
