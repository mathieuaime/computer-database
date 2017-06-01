package com.excilys.computerdatabase.daos.interfaces.template;

import java.util.List;

public interface PageDAO<T> {
    
    /**
     * Returns the list of the computers.
     * @return List Computer
     */
    List<T> findAll();

    /**
     * Returns the list of the computers between offset and offset + length -1.
     * @param offset the first computer
     * @param length the number of computers
     * @param search the field to search for
     * @param sort the field to sort for
     * @param order the field to order by
     * @return List Computer
     */
    List<T> findAll(int offset, int length, String search, String sort, String order);
    
    /**
     * Return the number of computers.
     * @return int number of computers
     * @param search the field to search for
     */
    int count(String search);
}
