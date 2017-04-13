package main.com.excilys.computerdatabase.interfaces;

import main.com.excilys.computerdatabase.dtos.Page;

public interface PageServ<T> {

    /**
     * Returns a page with all the objects.
     * @return Page<T>
     */
    Page<T> getPage();

    /**
     * Returns the page numero pageNumero of length length.
     * @param pageNumero the numero of the page
     * @param length the length of the page
     * @return Page<T>
     */
    Page<T> getPage(int pageNumero, int length);
}