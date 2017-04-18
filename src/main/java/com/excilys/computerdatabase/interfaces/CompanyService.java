package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;

public interface CompanyService {

    /**
     * Returns the list of the companies.
     * @return List<Company>
     */
    List<CompanyDTO> get();

    /**
     * Returns the company id.
     * @param id the id of the company
     * @return Company
     */
    CompanyDTO get(int id);

    /**
     * Returns the list of the computers of the company id.
     * @param id the id of the company
     * @return List<Computer>
     */
    List<ComputerDTO> getComputers(int id);
}
