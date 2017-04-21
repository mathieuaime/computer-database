package com.excilys.computerdatabase.services;

import java.util.List;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.daos.CompanyDAOImpl;
import com.excilys.computerdatabase.daos.ComputerDAOImpl;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.interfaces.ComputerService;
import com.excilys.computerdatabase.interfaces.PageServ;
import com.excilys.computerdatabase.models.Computer;

public class ComputerServiceImpl implements ComputerService, PageServ<ComputerDTO> {

    private ComputerDAOImpl computerDAO;
    private CompanyDAOImpl companyDAO;

    /**
     * ComputerService constructor.
     */
    public ComputerServiceImpl() {
        computerDAO = new ComputerDAOImpl();
    }

    /**
     * ComputerService constructor with a custom url.
     * @param url the url of the connexion.
     */
    public ComputerServiceImpl(String url) {
        computerDAO = new ComputerDAOImpl(url);
    }

    @Override
    public List<ComputerDTO> get() {
        return computerDAO.findAll().stream().map(it -> computerDAO.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public Page<ComputerDTO> getPage() {
        return new Page<ComputerDTO>(get(), 1);
    }

    @Override
    public Page<ComputerDTO> getPage(int pageNumero, int length) {

        return getPage(pageNumero, length, null, Computer.FIELD_NAME, "ASC");
    }

    @Override
    public Page<ComputerDTO> getPage(int pageNumero, int length, String search, String sort, String order) {

        List<ComputerDTO> l = computerDAO.findAll((pageNumero - 1) * length, length, search, sort, order).stream()
                .map(it -> computerDAO.createDTO(it)).collect(Collectors.toList());

        return new Page<ComputerDTO>(l, pageNumero);
    }

    @Override
    public ComputerDTO getById(long id) throws ComputerNotFoundException {
        Computer computer = computerDAO.getById(id);

        if (computer == null) {
            throw new ComputerNotFoundException("Computer Not Found");
        }

        return computerDAO.createDTO(computer);
    }

    @Override
    public List<ComputerDTO> getByName(String name) {
        return computerDAO.getByName(name).stream().map(it -> computerDAO.createDTO(it)).collect(Collectors.toList());
    }

    @Override
    public void add(ComputerDTO computerDTO) throws IntroducedAfterDiscontinuedException, NameEmptyException {
        Computer computer = computerDAO.createBean(computerDTO);
        computerDAO.add(computer);
    }

    @Override
    public void update(ComputerDTO computerDTO) throws IntroducedAfterDiscontinuedException, ComputerNotFoundException, NameEmptyException {
        Computer computer = computerDAO.createBean(computerDTO);
        computerDAO.update(computer);
    }

    @Override
    public void delete(long id) throws ComputerNotFoundException {
        computerDAO.delete(id);
    }
    
    @Override
    public void delete(List<Long> ids) throws ComputerNotFoundException {
        computerDAO.delete(ids);
    }

    @Override
    public int count(String search) {
        return computerDAO.count(search);
    }

    @Override
    public CompanyDTO getCompany(long id) {
        return companyDAO.createDTO(computerDAO.getCompany(id));
    }
}
