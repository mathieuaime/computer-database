package com.excilys.computerdatabase.daos.interfaces;

import java.util.List;

import com.excilys.computerdatabase.daos.interfaces.template.CrudDAO;
import com.excilys.computerdatabase.daos.interfaces.template.PageDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface CompanyDAO extends CrudDAO<Company>, PageDAO<Company> {
    /**
     * Returns the computer of the company id.
     * @param id id of the company
     * @return List Computer
     * @throws CompanyNotFoundException Company Not Found
     */
    List<Computer> getComputers(long id) throws CompanyNotFoundException;
}
