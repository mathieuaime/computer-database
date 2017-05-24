package com.excilys.computerdatabase.services.interfaces;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;

@Secured("ROLE_USER")
public interface CompanyService extends PageService<CompanyDTO> {

    /**
     * Returns the company id.
     * @param id the id of the company
     * @return CompanyDTO
     * @throws CompanyNotFoundException Company Not Found
     */
    CompanyDTO getById(long id) throws CompanyNotFoundException;

    /**
     * Returns the list of companies named name.
     * @param name the name of the company
     * @return List CompanyDTO
     */
    List<CompanyDTO> getByName(String name);

    /**
     * Returns the list of the computers of the company id.
     * @param id the id of the company
     * @return List ComputerDTO
     * @throws CompanyNotFoundException Company Not Found
     */
    List<ComputerDTO> getComputers(long id) throws CompanyNotFoundException;

    /**
     * Delete the company id and all its computers.
     * @param id the id of the company
     * @throws CompanyNotFoundException when the company does not exist
     */
    @Secured("ROLE_ADMIN")
    void delete(long id) throws CompanyNotFoundException;
}
