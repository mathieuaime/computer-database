package com.excilys.computerdatabase.mappers.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.Page;

@Component
public class PageMapper {

    @Autowired
    private ComputerMapper computerMapper;

    public Page<ComputerDTO> dto(Page<Computer> page) {

        List<Computer> list = page.getObjects();
        List<ComputerDTO> listDto = new ArrayList<>(list.size());
        for (Computer computer : list) {
            listDto.add(computerMapper.dto(computer));
        }
        return new Page<>(listDto, page.getPage(), page.getPageSize());
    }
}
