package com.excilys.computerdatabase.services.interfaces.template;

import org.springframework.security.access.annotation.Secured;

import com.excilys.computerdatabase.models.Page;

//@Secured("ROLE_USER")
public interface PageService<T> {

    /**
     * Returns a page with all the objects.
     * @return Page T
     */
    Page<T> getPage();

    /**
     * Returns the page numero pageNumero of length length.
     * @param page the parameters of the page
     * @return Page T
     */
    Page<T> getPage(Page<?> page);
    

    /**
     * Return the number of computers.
     * @return int number of computers
     * @param search the field to search for
     */
    int count(String search);
}