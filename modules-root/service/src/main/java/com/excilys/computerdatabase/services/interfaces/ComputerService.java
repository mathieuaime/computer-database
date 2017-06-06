package com.excilys.computerdatabase.services.interfaces;

import org.springframework.security.access.annotation.Secured;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.services.interfaces.template.CrudService;
import com.excilys.computerdatabase.services.interfaces.template.PageService;

//@Secured("ROLE_USER")
public interface ComputerService extends CrudService<Computer, Computer>, PageService<Computer> {
    /**
     * Returns the company of the computer id.
     * @param id the id of the computer
     * @return CompanyDTO
     * @throws CompanyNotFoundException exception when the company is not found
     * @throws ComputerNotFoundException exception when the computer is not found
     */
    Company getCompany(long id) throws NotFoundException;
}
