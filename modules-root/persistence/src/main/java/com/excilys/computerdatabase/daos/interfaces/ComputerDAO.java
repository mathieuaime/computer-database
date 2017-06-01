package com.excilys.computerdatabase.daos.interfaces;

import com.excilys.computerdatabase.daos.interfaces.template.CrudDAO;
import com.excilys.computerdatabase.daos.interfaces.template.PageDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

public interface ComputerDAO extends CrudDAO<Computer>, PageDAO<Computer> {
    /**
     * Returns the Company of the computer id.
     * @param id the id of the computer
     * @return Company
     * @throws CompanyNotFoundException when the company doesn't exist
     * @throws ComputerNotFoundException when the computer doesn't exist
     */
    Company getCompany(long id) throws CompanyNotFoundException, ComputerNotFoundException;

    /**
     * Delete the computer of the company id.
     * @param companyId the company id
     * @throws CompanyNotFoundException when the company doesn't exist
     */
    void deleteFromCompany(long companyId) throws CompanyNotFoundException;
}
